package com.coupon.document;

public class Product {
	private long productId;
	private String productName;
	private double productPrice;
	private String productImage;
	private String productDescription;
	private String productDuration;
	private String suiteName;

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productImage=" + productImage + ", productDescription=" + productDescription + ", productDuration="
				+ productDuration + ", suiteName=" + suiteName + "]";
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductDuration() {
		return productDuration;
	}

	public void setProductDuration(String productDuration) {
		this.productDuration = productDuration;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

}
