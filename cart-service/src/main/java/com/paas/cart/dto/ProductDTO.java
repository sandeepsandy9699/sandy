package com.paas.cart.dto;

import java.sql.Timestamp;

import com.mongodb.gridfs.GridFSDBFile;

public class ProductDTO {

	private long productId;

	private String productName;
	private double productPrice;
	private String productDescription;
	private String productURL;
	private String productIcon;
	private String message;
	private GridFSDBFile gridFSDBFile;
	private String productImage;
	private Timestamp timeStamp;
	private String startDate;
	private String endDate;
	private String assignedBy;
	private String productDuration;
	private boolean trial;
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignedBy == null) ? 0 : assignedBy.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((gridFSDBFile == null) ? 0 : gridFSDBFile.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
		result = prime * result + ((productDuration == null) ? 0 : productDuration.hashCode());
		result = prime * result + ((productIcon == null) ? 0 : productIcon.hashCode());
		result = prime * result + (int) (productId ^ (productId >>> 32));
		result = prime * result + ((productImage == null) ? 0 : productImage.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		long temp;
		temp = Double.doubleToLongBits(productPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((productURL == null) ? 0 : productURL.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + (trial ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDTO other = (ProductDTO) obj;
		if (assignedBy == null) {
			if (other.assignedBy != null)
				return false;
		} else if (!assignedBy.equals(other.assignedBy))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (gridFSDBFile == null) {
			if (other.gridFSDBFile != null)
				return false;
		} else if (!gridFSDBFile.equals(other.gridFSDBFile))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (productDescription == null) {
			if (other.productDescription != null)
				return false;
		} else if (!productDescription.equals(other.productDescription))
			return false;
		if (productDuration == null) {
			if (other.productDuration != null)
				return false;
		} else if (!productDuration.equals(other.productDuration))
			return false;
		if (productIcon == null) {
			if (other.productIcon != null)
				return false;
		} else if (!productIcon.equals(other.productIcon))
			return false;
		if (productId != other.productId)
			return false;
		if (productImage == null) {
			if (other.productImage != null)
				return false;
		} else if (!productImage.equals(other.productImage))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (Double.doubleToLongBits(productPrice) != Double.doubleToLongBits(other.productPrice))
			return false;
		if (productURL == null) {
			if (other.productURL != null)
				return false;
		} else if (!productURL.equals(other.productURL))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (trial != other.trial)
			return false;
		return true;
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
