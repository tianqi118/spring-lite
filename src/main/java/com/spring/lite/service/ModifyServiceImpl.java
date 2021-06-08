package com.spring.lite.service;

import com.spring.lite.framework.annotation.GPService;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-08
 */
@GPService
public class ModifyServiceImpl implements IModifyService {
    @Override
    public String add(String name, String address) {
        String result = "addService name:" + name + " address:" + address;
        System.out.println(result);
        return result;
    }

    @Override
    public String edit(Integer id, String name) {
        String result = "editService id:" + id + " name:" + name;
        System.out.println(result);
        return result;
    }

    @Override
    public String del(Integer id) {
        String result = "delService id:" + id;
        System.out.println(result);
        return result;
    }
}
