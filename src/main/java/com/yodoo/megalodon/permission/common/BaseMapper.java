package com.yodoo.megalodon.permission.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Date 2019/6/10 20:03
 * @author  by houzhen
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
