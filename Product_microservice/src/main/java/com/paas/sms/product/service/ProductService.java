package com.paas.sms.product.service;

import com.paas.sms.product.model.Product;


public interface ProductService {
	
	public Iterable<Product> getAllProducts();

	public Product getProduct(long id);

	public Product save(Product product);



}
