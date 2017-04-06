package com.shop.manage.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.vo.ItemCatData;
import com.shop.common.vo.ItemCatResult;
import com.shop.manage.pojo.ItemCat;
import com.shop.manage.service.ItemCatService;

@Controller
public class WebItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	//查询所有的分类，构造ItemCatData和ItemCatResult	web/itemcat/all
	@RequestMapping("web/itemcat/all")
	@ResponseBody	//返回必须是json，回调函数交给springmvc
	public ItemCatResult queryItemCatAll(){
		ItemCatResult result = new ItemCatResult();
		//获取所有菜单，构建所有菜单的子的集合item，Map(id,List<ItemCat>)
		Map<Long,List<ItemCat>> cats = new HashMap<Long,List<ItemCat>>();
		List<ItemCat> itemCatList = itemCatService.queryAll();
		
		for(ItemCat cat : itemCatList){
			//如果父节点idmap不存在，创建一个空的
			if(!cats.containsKey(cat.getParentId())){
				cats.put(cat.getParentId(), new ArrayList<ItemCat>());
			}
			cats.get(cat.getParentId()).add(cat);	//把当前分类放入父分类集合中
		}

		//构建一级菜单
		
		for(ItemCat cat : cats.get(0L)){	//一级菜单获取
			ItemCatData itemCatData = new ItemCatData();	//一条菜单
			itemCatData.setUrl("/products/"+cat.getId()+".html");
			itemCatData.setName("<a href=\"/products/"+cat.getId()+".html\">"+cat.getName()+"</a>");
			
			//构建二级菜单
			List<ItemCatData> catList2 = new ArrayList<ItemCatData>();	//二级菜单的所有子菜单
			for(ItemCat cat2 : cats.get(cat.getId())){
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/"+cat2.getId()+".html");
				itemCatData2.setName(cat2.getName());
				
				//构建三级菜单
				List<String> catList3 = new ArrayList<String>();
				for(ItemCat cat3 : cats.get(cat2.getId())){
					catList3.add("/products/"+cat3.getId()+".html|"+cat3.getName());	//把三级一条菜单构建完毕，把它加入到二级菜单i集合中
				}
				
				itemCatData2.setItems(catList3);
				catList2.add(itemCatData2);		//构建二级菜单集合
			}
			itemCatData.setItems(catList2);
			
			result.getItemCats().add(itemCatData);	//把一级菜单放入最终集合
			
			if(result.getItemCats().size()>14){		//如果一级菜单超过14个，页面就无法显示，直接退出
				break;
			}
		}
		
		return result;
	}
}
