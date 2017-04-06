package com.shop.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//这是一个通用结构，可以转向WEB-INF/views下的所有的jsp，利用RESTFul形式
@Controller 	//http://localhost:8081/page/index
public class PageController {
	//通用转向
	@RequestMapping("/page/{pageName}")	//RESTFul的占位符
	public String goHome(@PathVariable String pageName){
		return pageName;	//内部资源视图解析器：prefix(/WEB-INF/views/)+logicName+suffix(.jsp)
	}
}
