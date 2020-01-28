package com.paas.sms.tenantservice.model;

public class ContactUs {

	private String firstName;
	private String lastName;
	private String email;
	private String subject;
	private String replymsg;
	private String message;



	@Override
	public String toString() {
		return "ContactUs [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", subject="
				+ subject + ", replymsg=" + replymsg + ", message=" + message + "]";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReplymsg() {
		return replymsg;
	}

	public void setReplymsg(String replymsg) {
		this.replymsg = replymsg;
	}

}
