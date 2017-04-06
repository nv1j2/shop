package com.shop.manage.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.common.vo.EasyUIResult;
import com.shop.common.vo.SysResult;
import com.shop.manage.pojo.Item;
import com.shop.manage.pojo.ItemDesc;
import com.shop.manage.service.ItemDescService;
import com.shop.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService ;
    
    @Autowired
    private ItemDescService itemDescService;
   

    @RequestMapping("/query")
    @ResponseBody
    public EasyUIResult query(@RequestParam("page")Integer page,@RequestParam("rows") Integer rows) {
        return this.itemService.queryList(page, rows);
    }
    
    //商品的新增
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public SysResult saveItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams) {

        item.setCreated(new Date());
        item.setUpdated(item.getCreated());
        item.setStatus(1);
        if (item.getId() != null) {
            LOGGER.warn("传入的商品数据中包含id数据! id = {}", item.getId());
        }
        item.setId(null);// 安全方面考虑，强制设置id为null
        LOGGER.info("新增商品数据! title = {}, cid = {}", item.getTitle(), item.getCid());

        // 保存到数据库
        try {
            this.itemService.saveItem(item, desc, itemParams);
        } catch (Exception e) {
            LOGGER.error("保存失败! title = " + item.getTitle(), e);
            // 返回错误状态
            return SysResult.build(500, "新增商品数据失败!");
        }

        if (LOGGER.isDebugEnabled()) {// 判断debug是否启用
            LOGGER.debug("商品添加成功! item = " + item);
        }

        return SysResult.ok();
    }
    
    
    //商品的修改
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public SysResult updateItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams) {
        
        try {
            this.itemService.updateItem(item, desc, itemParams);
            return SysResult.ok();
        } catch (Exception e) {
            // TODO: handle exception
            return SysResult.build(201, "修改商品失败");
        }

    }
    
    
    //商品的删除
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public SysResult delete(@RequestParam("ids") Long[] ids ) {
        try {
            itemService.deleteItemByIds(ids);
            return SysResult.ok();
        } catch (Exception e) {
            // TODO: handle exception
            return SysResult.build(201, "删除失败");
        }

    }
    
    //商品详细信息查询
    @RequestMapping(value = "query/item/desc/{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public SysResult queryItemDescByItemId(@PathVariable("itemId") Long itemId) {
        ItemDesc itemDesc = this.itemDescService.queryById(itemId);
        return SysResult.ok(itemDesc);
    }
    

    
}
