package com.spring.lite.framework.webmvc;

import java.io.File;
import java.util.Locale;

/**
 * @author wangtianqi20
 * @Description 静态文件变动态文件
 * 根据不同的参数返回不同的效果
 * 输出字符串交给response
 * @date 2021-06-08
 */

public class GPViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    private File templateRootDir;
    private String viewName;

    public GPViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRoot);
    }

    public GPView resolverViewName(String viewName, Locale locale) throws Exception {

        this.viewName = viewName;
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);

        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", ""));
        return new GPView(templateFile);
    }

    public String getViewName() {
        return viewName;
    }

}
