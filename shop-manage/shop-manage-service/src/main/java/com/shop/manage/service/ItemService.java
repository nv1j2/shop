package com.shop.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.common.vo.EasyUIResult;
import com.shop.manage.mapper.ItemDescMapper;
import com.shop.manage.mapper.ItemMapper;
import com.shop.manage.pojo.Item;
import com.shop.manage.pojo.ItemDesc;

@Service
public class ItemService extends BaseService<Item>{
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private ItemDescService itemDescService;
    
    @Autowired
    private ItemDescMapper itemDescMapper;
    
//    @Autowired
//    private RedisService redisService ;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public EasyUIResult queryList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows, true);// 设置分页信息

        List<Item> items = itemMapper.queryItemList();// 按照更新时间倒序排序
        PageInfo<Item> pageInfo = new PageInfo<Item>(items);

        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }
    public List<Item> queryItemList(){
        return itemMapper.queryItemList();
    }

    public void saveItem(Item item, String desc, String itemParams) {
        // TODO Auto-generated method stub

        itemMapper.insertSelective(item);   //通用Mapper新增
        // 保存商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(itemDesc.getCreated());
        // 保存商品描述数据到数据库
        itemDescMapper.insertSelective(itemDesc);   //保存商品详情
        
    }


    public void updateItem(Item item, String desc, String itemParams) {
        // TODO Auto-generated method stub

        item.setUpdated(new Date());

        item.setCreated(null);// 强制设置为null
        itemMapper.updateByPrimaryKeySelective(item);
        
        // 更新商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setUpdated(new Date());
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        //redisService.del("ITEM_"+item.getId()) ;
        String routingKey = "item.update";  //路由key
        //发送消息到交换机
        rabbitTemplate.convertAndSend(routingKey, "ITEM_"+item.getId()); 
        
    }


    public void deleteItemByIds(Long[] ids) {
        // TODO Auto-generated method stub
        itemDescMapper.deleteByIDS(ids);    //先删除子表的，在删除主表
        itemDescService.deleteByIds(ids);
    }
    //根据商品的id查询对应的商品详情
    public ItemDesc getItemDesc(Long itemId){
        return itemDescMapper.selectByPrimaryKey(itemId);
    }
}
