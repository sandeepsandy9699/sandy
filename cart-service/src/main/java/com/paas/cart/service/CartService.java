package com.paas.cart.service;

import java.util.List;

import com.paas.cart.document.Cart;

public interface CartService {
	
	public Cart addToCart(Cart c);

	List<Cart> getCart();
	
	Cart findByUsername(String username);
	
	void deleteCart(String username);
	

}
