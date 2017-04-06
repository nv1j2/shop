package com.shop.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.manage.pojo.ItemCat;
import com.shop.manage.service.ItemCatService;

@Controller
@RequestMapping("/item")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	//查询所有的商品分类，返回json
	@RequestMapping("/queryall")
	//jackson ObjectMapper.writerAsValue(obj);	把一对java对象转成josn串
	@ResponseBody	//springmvc直接把返回对象转成成json串，背后就是通过jackson实现
	public List<ItemCat> queryAll(){
		List<ItemCat> itemCatList = itemCatService.queryAll();
		
		return itemCatList;
	}
	
	//列表，树结构，返回json	/item/cat/list
	@RequestMapping("/cat/list")
	@ResponseBody	//id EasyUI.tree给定	//第一次查询根节点下数据，第二次以后就按当前节点id进行查询
	public List<ItemCat> list(@RequestParam(defaultValue="0")Long id){
		ItemCat param = new ItemCat();
		param.setParentId(id);
		
		List<ItemCat> itemCatList = itemCatService.queryListByWhere(param);
		return itemCatList;
	}
}
