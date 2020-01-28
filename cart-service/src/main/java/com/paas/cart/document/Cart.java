package com.paas.cart.document;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.paas.cart.dto.ProductDTO;

@Document(collection = "Cart")
public class Cart {

	@Id
	private String username;

	private Set<ProductDTO> uniqueProductDTO;

	@Override
	public String toString() {
		return "Cart [username=" + username + ", uniqueProductDTO=" + uniqueProductDTO + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<ProductDTO> getUniqueProductDTO() {
		return uniqueProductDTO;
	}

	public void setUniqueProductDTO(Set<ProductDTO> uniqueProductDTO) {
		this.uniqueProductDTO = uniqueProductDTO;
	}

}
