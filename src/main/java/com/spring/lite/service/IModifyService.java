package com.spring.lite.service;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-08
 */

public interface IModifyService {

    /**
     * 增加
     *
     * @param name
     * @param address
     * @return
     */
    String add(String name, String address);

    /**
     * 修改
     *
     * @param id
     * @param name
     * @return
     */
    String edit(Integer id, String name);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    String del(Integer id);
}
