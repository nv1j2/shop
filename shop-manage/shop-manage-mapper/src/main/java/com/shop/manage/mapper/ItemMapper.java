package com.shop.manage.mapper;

import java.util.List;

import com.shop.common.mapper.SysMapper;
import com.shop.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item> {
    public List<Item> queryItemList();  //查询商品列表，按修改时间倒叙

    public List<Item> queryListOrderByUpdated();

}
