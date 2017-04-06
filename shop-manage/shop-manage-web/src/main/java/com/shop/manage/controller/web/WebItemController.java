package com.shop.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.manage.pojo.Item;
import com.shop.manage.service.ItemService;

@Controller
public class WebItemController {
	@Autowired
	private ItemService itemService;
	
	//获取商品信息，返回json串（主流json）
	@RequestMapping("/item/{itemId}")	//RESTFul请求
	@ResponseBody
	public Item getItemById(@PathVariable Long itemId){
		return itemService.queryById(itemId);
	}
}
