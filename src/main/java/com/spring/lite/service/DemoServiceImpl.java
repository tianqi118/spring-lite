package com.spring.lite.service;

import com.spring.lite.annotation.GPService;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-05-25
 */
@GPService
public class DemoServiceImpl implements IDemoService {

    @Override
    public String get(String name) {
        return "receive:" + name;
    }
}
