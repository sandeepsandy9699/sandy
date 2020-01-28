package com.paas.license.dto;

import java.sql.Timestamp;

import com.mongodb.gridfs.GridFSDBFile;

public class ProductDTO {

	private long productId;

	private String productName;
	private double productPrice;
	private String productDescription;
	private String productIcon;
	private String productURL;
	private String message;
	private GridFSDBFile gridFSDBFile;
	private String productImage;
	private Timestamp timeStamp;
	private String startDate;
	private String productDuration;
	private String trialDuration;
	private String paymentMode;
	private String transactionNumber;
	private boolean trial;
	private String endDate;
	private int extensionDays;
	private String assignedBy;
	private String subscribedBy;

	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productDescription=" + productDescription + ", productIcon=" + productIcon + ", productURL="
				+ productURL + ", message=" + message + ", gridFSDBFile=" + gridFSDBFile + ", productImage="
				+ productImage + ", timeStamp=" + timeStamp + ", startDate=" + startDate + ", productDuration="
				+ productDuration + ", trialDuration=" + trialDuration + ", paymentMode=" + paymentMode
				+ ", transactionNumber=" + transactionNumber + ", trial=" + trial + ", endDate=" + endDate
				+ ", extensionDays=" + extensionDays + ", assignedBy=" + assignedBy + ", subscribedBy=" + subscribedBy
				+ "]";
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

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GridFSDBFile getGridFSDBFile() {
		return gridFSDBFile;
	}

	public void setGridFSDBFile(GridFSDBFile gridFSDBFile) {
		this.gridFSDBFile = gridFSDBFile;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public String getProductDuration() {
		return productDuration;
	}

	public void setProductDuration(String productDuration) {
		this.productDuration = productDuration;
	}

	public boolean isTrial() {
		return trial;
	}

	public void setTrial(boolean trial) {
		this.trial = trial;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getExtensionDays() {
		return extensionDays;
	}

	public void setExtensionDays(int extensionDays) {
		this.extensionDays = extensionDays;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getTrialDuration() {
		return trialDuration;
	}

	public void setTrialDuration(String trialDuration) {
		this.trialDuration = trialDuration;
	}

	public String getProductIcon() {
		return productIcon;
	}

	public void setProductIcon(String productIcon) {
		this.productIcon = productIcon;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getSubscribedBy() {
		return subscribedBy;
	}

	public void setSubscribedBy(String subscribedBy) {
		this.subscribedBy = subscribedBy;
	}

}
