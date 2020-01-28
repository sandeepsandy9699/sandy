package com.paas.sms.product.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.swing.ImageIcon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.gridfs.GridFSDBFile;

/*import lombok.Data;
@Data
*/
@Document(collection = "Product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long productId;
	@Column(unique = true)
	private String searchProductName;

	private String productName;
	private double productPrice;
	private String productIcon;

	private String productDescription;
	private String productURL;
	private String productDuration;
	private String trialDuration;
	private String message;
	private GridFSDBFile gridFSDBFile;
	private String productImage;
	private Timestamp timeStamp;
	private String startDate;
	private String endDate;
	private String assignedBy;
	

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", searchProductName=" + searchProductName + ", productName="
				+ productName + ", productPrice=" + productPrice + ", productIcon=" + productIcon
				+ ", productDescription=" + productDescription + ", productURL=" + productURL + ", productDuration="
				+ productDuration + ", trialDuration=" + trialDuration + ", message=" + message + ", gridFSDBFile="
				+ gridFSDBFile + ", productImage=" + productImage + ", timeStamp=" + timeStamp + ", startDate="
				+ startDate + ", endDate=" + endDate + ", assignedBy=" + assignedBy + "]";
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSearchProductName() {
		return searchProductName;
	}

	public void setSearchProductName(String searchProductName) {
		this.searchProductName = searchProductName;
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

}
