package com.paas.license.dto;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


public class UserDTO {

	@Id
	private long userId;

	private String firstname;

	private String lastname;

	private String userImage;

	// @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups =
	// true)
	private String email;

	private String mobilenumber;

	private String extensionNumber;
	private String role_alias;
	private String client_master;
	private String site_admin;
	private String site_user;

	private String username;

	private String organizationName;

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	private String password;

	private String passwordConfirm;

	private String country;

	private String state;
	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	private String city;

	private boolean isVerified;

	private String role;

	private String createdBy;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}



	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRole_alias() {
		return role_alias;
	}

	public void setRole_alias(String role_alias) {
		this.role_alias = role_alias;
	}

	public String getClient_master() {
		return client_master;
	}

	public void setClient_master(String client_master) {
		this.client_master = client_master;
	}

	public String getSite_admin() {
		return site_admin;
	}

	public void setSite_admin(String site_admin) {
		this.site_admin = site_admin;
	}

	public String getSite_user() {
		return site_user;
	}

	public void setSite_user(String site_user) {
		this.site_user = site_user;
	}
}
