package com.paas.license.document;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.paas.license.dto.ProductDTO;

@Document(collection = "License")
public class License {

	@Id
	private String username;

	private Set<ProductDTO> uniqueProducts;

	@Override
	public String toString() {
		return "License [username=" + username + ", uniqueProducts=" + uniqueProducts + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<ProductDTO> getUniqueProducts() {
		return uniqueProducts;
	}

	public void setUniqueProducts(Set<ProductDTO> uniqueProducts) {
		this.uniqueProducts = uniqueProducts;
	}

}
