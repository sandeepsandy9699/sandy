package com.paas.sms.tenantservice.model;

public class CountryDetails {

	private String extensionNumber;
	private int countryId;
	private String countryCode;
	private String countryName;

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Override
	public String toString() {
		return "CountryDetails [extensionNumber=" + extensionNumber + ", countryId=" + countryId + ", countryCode="
				+ countryCode + ", countryName=" + countryName + "]";
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

}
