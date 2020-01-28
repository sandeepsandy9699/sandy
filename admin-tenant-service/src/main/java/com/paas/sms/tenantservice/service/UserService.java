package com.paas.sms.tenantservice.service;

import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.paas.sms.tenantservice.document.User;
import com.paas.sms.tenantservice.document.UserNotification;
import com.paas.sms.tenantservice.model.Email;
import com.paas.sms.tenantservice.model.Notification;
import com.paas.sms.tenantservice.model.OTP;
import com.paas.sms.tenantservice.model.SigninDTO;

public interface UserService {

	public User save(User user);

	public User search(String username);

	public SigninDTO signin(String username, String password);

	public User deleteUser(User user);

	public String signup(User admin) throws Exception;

	public Object whoami(HttpServletRequest req);

	public String refresh(String Username);

	public User findByEmail(String emailId);

	public Notification readNotification(long id);

	public UserNotification readUserNotification(long notificationId);

	public User findByUsername(String userName);

	public String findByRole(String role);

	public List<Notification> getNotifications();

	public String deleteAllNotifications();

	public String deleteNotification(long notificationId);

	public long getNotificationCount();

	public List<User> findAllUsers();

	public User findById(long userId);

	public void emailUpdate(User user) throws Exception;

	public User savePassword(User user);

	public String isEnabled(User user);

	public String confirmUserAccount(String confirmationToken);

	public void saveQuery(Notification notification);

	public void saveNotification(UserNotification userNotification);

	public String generateToken(User user);

	public OTP forgotPassword(String userName) throws Exception;

	public List<?> queryArrayElement(String username) throws UnknownHostException;

	public String getSiteAdmins(String role);

	public String getSiteUsers(String clientMaster, String siteAdmin);

	public String getAdminsAndUsers(String clientMaster);

	public String deleteUserNotification(long notificationId);

	public String getAdminUsers(String siteAdmin);

	public List<UserNotification> getUserNotifications(String email);

	public void saveEmail(Email email);

	public void updateMail(String oldMail, String newMail);

	public List<Email> getSentEmails(String email);

	public String deleteSentMail(long id);

	public Email readSentMail(long id);

}
