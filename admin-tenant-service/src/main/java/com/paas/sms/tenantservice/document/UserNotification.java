package com.paas.sms.tenantservice.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserNotification")
public class UserNotification {
	
	@Id
	private long notificationId;
	private String subject;
	private String message;
	private String email;
	private String emailFrom;
	private String date;
	private String time;
	private boolean read;

	



	@Override
	public String toString() {
		return "UserNotification [notificationId=" + notificationId + ", subject=" + subject + ", message=" + message
				+ ", email=" + email + ", emailFrom=" + emailFrom + ", date=" + date + ", time=" + time + ", read="
				+ read + "]";
	}

	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
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

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

}
