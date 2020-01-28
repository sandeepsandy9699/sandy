/*
 * 
 */
package com.iaas.sms.model;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
@Document(collection = "userMaster")
public class User extends BaseEntity implements UserDetails {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7954325925563724664L;

	/** The authorities. */
	private List<Authority> authorities;

	private String firstname;

	private String lastname;

	/** The username. */
	private String username;

	/** The password. */

	private String password;

	/** The email. */
	private String email;

	/** The mobilenumber. */
	private String mobilenumber;

	private String organizationName;

	private String role_alias;

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	private String extensionNumber;

	/** The password confirm. */
	private String passwordConfirm;

	private String client_master;
	private String site_admin;
	private String site_user;
	private String country;
	private String state;
	private String location;
	private String city;

	private String role;

	private String createdBy;

	/**
	 * Gets the authorities.
	 *
	 * @return the authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Checks if is account non expired.
	 *
	 * @return true, if is account non expired
	 */

	/**
	 * Checks if is credentials non expired.
	 *
	 * @return true, if is credentials non expired
	 */

	/**
	 * Sets the authorities.
	 *
	 * @param authorities the new authorities
	 */
	public void setAuthorities(final List<Authority> authorities) {
		this.authorities = authorities;
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
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
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
