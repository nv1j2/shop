package com.shop.sso.mapper;

import java.util.Map;

import com.shop.common.mapper.SysMapper;
import com.shop.sso.pojo.User;

public interface UserMapper extends SysMapper<User>{
	public Integer check(Map<String,String> map);
}
