/*
 * 
 */
package com.iaas.sms.dto;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class UserDTO.
 */
public class UserDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 91901774547107674L;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The email. */
	private String email;

	/** The mobilenumber. */
	private String mobilenumber;

	/** The password confirm. */
	private String passwordConfirm;
	
	private String role_alias;

	/** The role. */
	private String role;

	private String createdBy;
	
	private String client_master;
	private String site_admin;
	private String site_user;

	/**
	 * Instantiates a new user DTO.
	 */
	public UserDTO() {
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the mobilenumber.
	 *
	 * @return the mobilenumber
	 */
	public String getMobilenumber() {
		return mobilenumber;
	}

	/**
	 * Sets the mobilenumber.
	 *
	 * @param mobilenumber the mobilenumber to set
	 */
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	/**
	 * Gets the password confirm.
	 *
	 * @return the passwordConfirm
	 */
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	/**
	 * Sets the password confirm.
	 *
	 * @param passwordConfirm the passwordConfirm to set
	 */
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
