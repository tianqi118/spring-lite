package com.spring.lite.servelet;

import com.spring.lite.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-05-25
 */
public class GPDispatcherServletV2 extends HttpServlet {
    //保存所有扫描的类名
    private List<String> claassNames = new ArrayList<String>();
    //Ioc容器
    private Map<String, Object> ioc = new HashMap<String, Object>();
    //url与method关系
    private List<Handler> handlerMapping = new ArrayList<Handler>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            System.out.println("500 Exception!!");
            resp.getWriter().write("500 Exception " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Handler handler = getHandler(req);
        if (handler == null) {
            System.out.println("404 Not Found!!");
            resp.getWriter().write("404 Not Found!!");
            return;
        }

//        Class<?>[] paramTypes = handler

//        Method method = (Method) this.handlerMapping.get(0);
        Map<String, String[]> params = req.getParameterMap();
//        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
//        method.invoke(this.ioc.get(beanName), new Object[]{req, resp, params.get("name")[0]});
        System.out.println("doDispatch end");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("1.获取包路径");
        String packagePath = getPackPath(config);
        System.out.println("2.扫描路径下的类：" + packagePath);
        doScanner(packagePath);
        System.out.println("3.扫描类注入ioc容器");
        doInstance();
        System.out.println("4.依赖注入");
        doAutowired();
        System.out.println("5.初始化HandlerMapping");
        initHandlerMapping();
        System.out.println("6.GP MVC Framework is init Completed");

    }

    /**
     * 获取Handler
     *
     * @param request
     * @return
     * @throws Exception
     */
    private Handler getHandler(HttpServletRequest request) throws Exception {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        System.out.println("request url：" + url + " contextPath：" + contextPath);
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        System.out.println("after porcess url:" + url);
        for (Handler handler : handlerMapping) {
            try {
                Matcher matcher = handler.pattern.matcher(url);
                if (!matcher.matches()) {//如果没有匹配上则继续
                    continue;
                }
                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    /**
     * Handler内部类
     */
    private class Handler {

        protected Object controller;//保存方法对应的实例
        protected Method method;//保存映射的方法
        protected Pattern pattern;
        protected Map<String, Integer> paramIndexMapping;//参数顺序

        protected Handler(Pattern pattern, Object controller, Method method) {
            this.controller = controller;
            this.pattern = pattern;
            this.method = method;
            paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(method);
        }


        private void putParamIndexMapping(Method method) {
            //提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof GPRequestParam) {
                        String paramName = ((GPRequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }
            //提取方法中的request和response参数
            Class<?>[] paramType = method.getParameterTypes();
            for (int i = 0; i < paramType.length; i++) {
                Class<?> type = paramType[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }

        }


    }


    /**
     * 获取配置包路径
     *
     * @param config
     * @return
     */
    private String getPackPath(ServletConfig config) {
        InputStream is = null;
        try {
            Properties configContext = new Properties();
            String file = config.getInitParameter("contextConfigLocation");
            System.out.println("file:" + file);
            is = this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));
            configContext.load(new FileInputStream(file));
            String scanPackage = configContext.getProperty("scanPackage");
            return scanPackage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 扫描包路径下的类
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String clazzName = (scanPackage + "." + file.getName().replace(".class", ""));
//                System.out.println("扫描类：" + clazzName);
                claassNames.add(clazzName);
            }
        }
    }


    /**
     * 注入ioc容器
     */
    private void doInstance() {
        if (claassNames.isEmpty()) {
            return;
        }
        try {
            for (String className : claassNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(GPController.class)) {
                    Object instance = clazz.newInstance();
                    //模拟Spring服务首字母小写
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    System.out.println("Controller simpleName：" + beanName);
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(GPService.class)) {
                    GPService service = clazz.getAnnotation(GPService.class);
                    String beanName = service.value();
                    if ("".equals(beanName)) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    System.out.println("Service simpleName:" + beanName);
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    //根据类型自动赋值
                    for (Class<?> i : clazz.getInterfaces()) {
                        System.out.println("interface name :" + i.getName());
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception(i.getName() + "is Exists");
                        }
                        ioc.put(i.getName(), instance);
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 依赖注入
     */
    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获得所有字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(GPAutowired.class)) {
                    continue;
                }
                GPAutowired autowired = field.getAnnotation(GPAutowired.class);
                String beanName = autowired.value();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                System.out.println("Autowired name:" + beanName);
                field.setAccessible(true);
                try {
                    //用反射机制动态给字段赋值
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * 初始化handler
     */
    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(GPController.class)) {
                continue;
            }
            String baseUri = "";
            if (clazz.isAnnotationPresent(GPRequestMapping.class)) {
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                baseUri = requestMapping.value();
                System.out.println("load controller base url:" + baseUri);
            }
            //获取Method url位置
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                //没有加GPRequestMapping注解的方法忽略
                if (!method.isAnnotationPresent(GPRequestMapping.class)) {
                    continue;
                }
                GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                String regex = ("/" + baseUri + requestMapping.value()).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new Handler(pattern, entry.getValue(), method));
                System.out.println("Mapped: " + regex + "," + method);
            }

        }
    }


    /**
     * 首字母变小写
     *
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        //大小写字母的ASCII码相差32
        chars[0] += 32;
        return String.valueOf(chars);
    }

}