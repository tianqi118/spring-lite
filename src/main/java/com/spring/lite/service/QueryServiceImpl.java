package com.spring.lite.service;

import com.alibaba.fastjson.JSON;
import com.spring.lite.framework.annotation.GPService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-08
 */
@GPService
public class QueryServiceImpl implements IQueryService {
    @Override
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        Map map = new HashMap();
        map.put("time", time);
        map.put("name", name);
        return JSON.toJSONString(map);
    }
}
