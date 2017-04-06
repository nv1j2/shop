package com.shop.cart.mapper;

import com.shop.common.mapper.SysMapper;
import com.shop.cart.pojo.Cart;

public interface CartMapper extends SysMapper<Cart> {
	public void updateCart(Cart cart);
}
