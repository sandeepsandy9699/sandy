package com.paas.sms.tenantservice.model;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SentMail")
public class Email {
	
	@Id
	private long id;
	private String subject;
	private String message;
	private String emailFrom;
	private String date;
	private String time;
	private String[] emails;

	
	
	@Override
	public String toString() {
		return "Email [id=" + id + ", subject=" + subject + ", message=" + message + ", emailFrom=" + emailFrom
				+ ", date=" + date + ", time=" + time + ", emails=" + Arrays.toString(emails) + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
