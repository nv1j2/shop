package com.shop.web.threadlocal;

import com.shop.web.pojo.User;

//拦截器形成共享变量，有高并发问题，通过线程保护
public class UserThreadLocal {
	private static final ThreadLocal<User> USER = new ThreadLocal<User>();

    public static void set(User user) { 
        USER.set(user);
    }

    public static User get() {
        return USER.get();
    }

    //直接获取userId
	public static Long getUserId(){
		if(null!=USER){
			if(null!=USER.get()){
				return USER.get().getId();
			}
		}
		return null;
	}
}