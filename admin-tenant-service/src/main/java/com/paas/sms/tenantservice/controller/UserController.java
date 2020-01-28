package com.paas.sms.tenantservice.controller;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.paas.sms.tenantservice.document.Tenant;
import com.paas.sms.tenantservice.document.User;
import com.paas.sms.tenantservice.document.UserNotification;
import com.paas.sms.tenantservice.dto.TokenDTO;
import com.paas.sms.tenantservice.dto.UserDataDTO;
import com.paas.sms.tenantservice.exceptions.ExceptionResponse;
import com.paas.sms.tenantservice.model.ContactUs;
import com.paas.sms.tenantservice.model.Email;
import com.paas.sms.tenantservice.model.Notification;
import com.paas.sms.tenantservice.model.OTP;
import com.paas.sms.tenantservice.model.SigninDTO;
import com.paas.sms.tenantservice.repository.NotificationRepository;
import com.paas.sms.tenantservice.repository.SequenceRepository;
import com.paas.sms.tenantservice.security.JwtTokenProvider;
import com.paas.sms.tenantservice.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Value("${email}")
	String Email_Id;

	@Value("${ClientMailId}")
	String ClientMailId;

	@Value("${password}")
	String password;

	static MongoClient mongo = new MongoClient("localhost", 27017);

	String licenseUri = "http://localhost:2175/paaslicense/license/deactivate/";
	String licenceUri = "http://localhost:2175/paaslicense/license/";
	String cartUri = "http://localhost:2170/paascart/cart/";
	String CouponUri = "http://localhost:2246/coupon/deleteUserCoupon/";

	private static final String HOSTING_SEQ_KEY = "userId";

	public static Map<String, Integer> countryDetails = new HashMap<String, Integer>();

	static {
		String[] locales = Locale.getISOCountries();
		for (String countryCode : locales) {
			Locale locale = new Locale("", countryCode);
			PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
			Integer extensionNumber = phoneNumberUtil.getCountryCodeForRegion(locale.getCountry());
			countryDetails.put(locale.getDisplayCountry(), extensionNumber);

		}
	}

	@PostMapping("/isEnabled")
	public String isEnabled(@RequestBody User user) {
		return userService.isEnabled(user);
	}

	@GetMapping("/getUser/{userName}")
	public User getUser(@PathVariable String userName) {
		User user = userService.findByUsername(userName);
		if (user != null) {
			return user;
		}
		return null;
	}

	@GetMapping("/getSubTenants/{username}")
	public List<?> getSubTenants(@PathVariable String username) throws UnknownHostException {

		return userService.queryArrayElement(username);

	}

	@GetMapping("/nameByToken/{token}")
	public String getUserName(@PathVariable String token) throws Exception {
		String userName = jwtTokenProvider.getUsername(token);
		if (userName != null) {
			return userName;
		}
		return "Un Authorized user";
	}

	@GetMapping("/generateToken/{userName}")
	public ResponseEntity<TokenDTO> generateToken(@PathVariable String userName) throws Exception {
		User user = userService.findByUsername(userName);
		if(user==null) {
			//throw new Exception("You are no longer AI3O member..!!");
			return new ResponseEntity<TokenDTO>(HttpStatus.FORBIDDEN);
		}
		String token = userService.generateToken(user);

		TokenDTO dto = new TokenDTO();
		dto.setTokenName(token);
		return new ResponseEntity<TokenDTO>(dto, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public SigninDTO login(@RequestBody SigninDTO dto) {
		String username = dto.getUsername();
		String password = dto.getPassword();
		return userService.signin(username, password);
	}

	@PutMapping("/changePassword/{userName}")
	public User changePassword(@RequestBody Tenant tenant, @PathVariable String userName) throws Exception {
		User user2 = userService.findByUsername(userName);
		String oldpwd = user2.getPasswordConfirm();
		if (oldpwd.equals(tenant.getOldPassword())) {
			user2.setPassword(tenant.getNewPassword());
			user2.setPasswordConfirm(tenant.getNewPassword());
			return userService.savePassword(user2);
		} else {
			throw new Exception("Invalid old password");
		}

	}

	@PutMapping("/resetPassword/{userName}")
	public User resetPassword(@RequestBody Tenant tenant, @PathVariable String userName) {
		User user2 = userService.findByUsername(userName);
		user2.setPassword(tenant.getNewPassword());
		user2.setPasswordConfirm(tenant.getNewPassword());
		return userService.savePassword(user2);
	}

	@PostMapping("/signup")
	public String signup(@RequestBody User user) throws Exception {
		System.out.println("**************** Entered into SignUp in AdminTask***********");
		user.setUserId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
		return userService.signup(modelMapper.map(user, User.class));
	}

	@RequestMapping(value = "/confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmAccount(@RequestParam("token") String confirmationToken) {
		String msg = userService.confirmUserAccount(confirmationToken);
		if (msg != null) {
			return verificationSuccess();
		} else {
			return verificationFailed();
		}
	}

	@GetMapping("/getUsersByRole/{role}")
	public String getUsersByRole(@PathVariable String role) {

		String users = userService.findByRole(role);
		return users;

	}

	@GetMapping("/getSiteAdmins/{clientMaster}")
	public String getSiteAdmins(@PathVariable String clientMaster) {

		String users = userService.getSiteAdmins(clientMaster);
		return users;

	}

	@GetMapping("/getAdminsAndUsers/{clientMaster}")
	public String getAdminsAndUsers(@PathVariable String clientMaster) {

		String users = userService.getAdminsAndUsers(clientMaster);
		return users;

	}

	@GetMapping("/getSiteUsers/{clientMaster}/{siteAdmin}")
	public String getSiteUsers(@PathVariable String clientMaster, @PathVariable String siteAdmin) {

		String users = userService.getSiteUsers(clientMaster, siteAdmin);
		return users;
	}

	@GetMapping("/getAdminUsers/{siteAdmin}")
	public String getAdminUsers(@PathVariable String siteAdmin) {
		return userService.getAdminUsers(siteAdmin);
	}

	@GetMapping("/getTenantCount")
	public long getTenantCount() {
		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		long tenantCount = col.find().count();
		return tenantCount - 1;
	}

	@GetMapping("/getCountries")
	public static String getItems() {
		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("Countries");
		DBCollection col = db.getCollection("Country");
		List<String> items = new ArrayList<>();
		BasicDBObject fields = new BasicDBObject();
		fields.put("id", 1);
		fields.put("name", 1);
		fields.put("phone_code", 1);
		Iterator<DBObject> cursor = col.find(new BasicDBObject(), fields).iterator();
		try {
			while (cursor.hasNext()) {
				items.add(((BasicDBObject) cursor.next()).toJson());
			}
		} finally {
			((DBCursor) cursor).close();
		}

		return "[" + String.join(", ", items) + "]";
	}

	@GetMapping("/getStates/{countryId}")
	public static String getStates(@PathVariable long countryId) {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("Countries");
		DBCollection col = db.getCollection("State");
		List<String> items = new ArrayList<>();
		BasicDBObject fields = new BasicDBObject();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("country_id", countryId);
		fields.put("id", 1);
		fields.put("name", 1);
		fields.put("state_code", 1);
		Iterator<DBObject> cursor = col.find(whereQuery, fields).iterator();
		try {
			while (cursor.hasNext()) {
				items.add(((BasicDBObject) cursor.next()).toJson());
			}
		} finally {
			((DBCursor) cursor).close();
		}

		return "[" + String.join(", ", items) + "]";
	}

//FindIterable<document> cursor = restaurants.find().sort(new BasicDBObject("stars",1)).limit(5);
	@GetMapping("/getCities/{stateId}")
	public static String getCities(@PathVariable long stateId) {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("Countries");
		DBCollection col = db.getCollection("City");
		List<String> items = new ArrayList<>();
		BasicDBObject fields = new BasicDBObject();
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("state_id", stateId);
		fields.put("name", 1);
		Iterator<DBObject> cursor = col.find(whereQuery, fields).iterator();
		try {
			while (cursor.hasNext()) {
				items.add(((BasicDBObject) cursor.next()).toJson());
			}
		} finally {
			((DBCursor) cursor).close();
		}

		return "[" + String.join(", ", items) + "]";
	}

	@GetMapping("/getClientMastersCount")
	public long getClientMastersCount() {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("role", "ROLE_CLIENT_MASTER");
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	@GetMapping("/getAdminsAndUsersCount/{clientMaster}")
	public long getAdminsAndUsersCount(@PathVariable String clientMaster) {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("client_master", clientMaster);
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	@GetMapping("/getUserNotificationsCount/{email}")
	public long getUserNotificationsCount(@PathVariable String email) {
		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("UserNotification");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", email);
		whereQuery.put("read", false);
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	@GetMapping("/getUserNotifications/{email}")
	public List<UserNotification> getUserNotifications(@PathVariable String email) {
		return userService.getUserNotifications(email);

		/*
		 * @SuppressWarnings("deprecation") DB db = mongo.getDB("paas-admin-tenant");
		 * DBCollection col = db.getCollection("UserNotification"); List<String> items =
		 * new ArrayList<>(); BasicDBObject whereQuery = new BasicDBObject(); //
		 * BasicDBObject sort = new BasicDBObject("$sort", new //
		 * BasicDBObject("trial",false)); whereQuery.put("email", email);
		 * Iterator<DBObject> cursor = col.find(whereQuery).iterator(); try { while
		 * (cursor.hasNext()) { items.add(((BasicDBObject) cursor.next()).toJson());
		 * 
		 * } } finally { ((DBCursor) cursor).close(); }
		 * items.sort(Comparator.reverseOrder()); return "[" + String.join(", ", items)
		 * + "]";
		 */
	}

	@GetMapping("/getSentMails/{email}")
	public List<Email> getSentEmails(@PathVariable String email) {
		return userService.getSentEmails(email);
	}

	@GetMapping("/getSiteAdminsCount")
	public long getSiteAdminsCount() {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("role", "ROLE_SITE_ADMIN");
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	@GetMapping("/getSiteUsersCount/{clientmaster}")
	public long getSiteUsersCount(@PathVariable String clientmaster) {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("role", "ROLE_SITE_USER");
		whereQuery.put("client_master", clientmaster);
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	@GetMapping("/getAdminUsersCount/{siteadmin}")
	public long getAdminUsersCount(@PathVariable String siteadmin) {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("role", "ROLE_SITE_USER");
		whereQuery.put("site_admin", siteadmin);
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	@GetMapping("/getSiteAdminsCount/{clientmaster}")
	public long getSiteAdminsCount(@PathVariable String clientmaster) {

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("paas-admin-tenant");
		DBCollection col = db.getCollection("userMasterCopy");
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("role", "ROLE_SITE_ADMIN");
		whereQuery.put("client_master", clientmaster);
		int cursor = col.find(whereQuery).count();
		return cursor;
	}

	public ModelAndView verificationSuccess() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accountVerified");
		return modelAndView;
	}

	public ModelAndView verificationFailed() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("invalidLink");
		return modelAndView;
	}

	@PostMapping("/forgotPassword/{userName}")
	public OTP forgotPassword(@PathVariable String userName) throws Exception {
		OTP otp = userService.forgotPassword(userName);
		return otp;
	}

	@RequestMapping(value = "/resetPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PasswordReset() {

		return resetPassword();
	}

	public ModelAndView resetPassword() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("resetpwd");
		return modelAndView;
	}

	@GetMapping("/findAllUsers")
	public List<User> findAllUsers() {
		if (userService.findAllUsers() != null) {
			List<User> userList = userService.findAllUsers();
			List<User> users = userList.stream().filter(user -> !user.getRole().equalsIgnoreCase("ROLE_ADMIN"))
					.collect(Collectors.toList());
			return users;
			// users;
		}
		return Collections.emptyList();
	}

	@PutMapping("/user/update")
	public ResponseEntity<User> updateUser(@RequestBody User userDetails, HttpServletRequest request) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		System.out.println(" ********* Token *********** ::" + token);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		User user = userService.findById(userDetails.getUserId());

		if (userDetails.getMobilenumber() != null) {
			user.setMobilenumber(userDetails.getMobilenumber());
		}
		if (userDetails.getExtensionNumber() != null) {
			user.setExtensionNumber(userDetails.getExtensionNumber());
		}
		if (userDetails.getCountry() != null) {
			user.setCountry(userDetails.getCountry());
		}
		if (userDetails.getState() != null) {
			user.setState(userDetails.getState());
		}
		if (userDetails.getCity() != null) {
			user.setCity(userDetails.getCity());
		}
		if (userDetails.getLocation() != null) {
			user.setLocation(userDetails.getLocation());
		}
		if (userDetails.getOrganizationName() != null) {
			user.setOrganizationName(userDetails.getOrganizationName());
		}
		if (userDetails.getUserImage() != null) {
			user.setUserImage(userDetails.getUserImage());
		}
		if (userDetails.getSite_admin() != null) {
			user.setSite_admin(userDetails.getSite_admin());
		}
		if (userDetails.getClient_master() != null) {
			user.setClient_master(userDetails.getClient_master());
		}
		if (userDetails.getFirstname() != null) {
			user.setFirstname(userDetails.getFirstname());
		}
		if (userDetails.getLastname() != null) {
			user.setLastname(userDetails.getLastname());
		}
		if (!(user.getEmail().equalsIgnoreCase(userDetails.getEmail()))) {
			user.setVerified(false);
			userService.emailUpdate(userDetails);
			userService.updateMail(user.getEmail(), userDetails.getEmail());
			// RestTemplate restTemplate = new RestTemplate();
			// System.out.println("URL::" + cartUri + "update/" + userDetails.getEmail() +
			// "/" + user.getEmail());
			// restTemplate.exchange(cartUri + "update/" + userDetails.getEmail() + "/" +
			// user.getEmail(), HttpMethod.PUT, entity, String.class);
			// restTemplate.exchange(licenceUri + "update/" + userDetails.getEmail() + "/" +
			// user.getEmail(), HttpMethod.PUT, entity, String.class);
		}
		if (userDetails.getEmail() != null) {

			user.setEmail(userDetails.getEmail());
		}

		User updatedTenant = userService.save(user);

		return ResponseEntity.ok(updatedTenant);

	}

	@DeleteMapping("/delete/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable String userName, HttpServletRequest request) {
		User user = userService.findByUsername(userName);
		String emailId = user.getEmail();

		userService.deleteUser(user);
		System.out.println("User is deleted: " + user);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		String token = request.getHeader("Authorization");
		System.out.println(" ********* Token *********** ::" + token);
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(licenseUri + userName, HttpMethod.DELETE, entity, String.class);
		restTemplate.exchange(CouponUri + emailId, HttpMethod.DELETE, entity, String.class);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(new ExceptionResponse(new Date(), "Record deleted", userName));
		// return result;
	}

	@GetMapping("/tenants/email/{email}")
	public ResponseEntity<User> findByEmail(@PathVariable(value = "email") String emailId) {
		System.out.println("EmailId::" + emailId);
		User tenant = userService.findByEmail(emailId);
		return ResponseEntity.ok().body(tenant);
	}

	@RequestMapping(value = "/sendMail", method = { RequestMethod.POST })
	public void sendMail(@RequestBody Email email) throws MessagingException {
		try {
			System.out.println("Entered into send mail");

			// String Email_Id = "paas.team2019@gmail.com";
			// String password = "vytqiietngjpytfw";
			// Set mail properties
			Properties props = System.getProperties();
			String host_name = "smtp.gmail.com";
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host_name);
			// props.put("mail.smtp.user", Email_Id);
			// props.put("mail.smtp.password", password);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Email_Id, password);
				}
			});

			// Session session = Session.getDefaultInstance(props);
			// MimeMessage message = new MimeMessage(session);
			Message message = new MimeMessage(session);
			// MimeMessageHelper helper = new MimeMessageHelper(message, true);

			try {
				// Set email data
				message.setFrom(new InternetAddress(Email_Id, "AI3O"));
				System.out.println("from address  ::" + Email_Id);

				String[] to = email.getEmails();
				InternetAddress[] toAddress = new InternetAddress[to.length];

				/*
				 * message.addRecipients(Message.RecipientType.TO,
				 * InternetAddress.parse("spsoft.krishnareddy@gmail.com")); for (int i = 0; i <
				 * to.length; i++) { toAddress[i] = new InternetAddress(to[i]); }
				 */

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email_Id));
				for (int i = 0; i < to.length; i++) {
					toAddress[i] = new InternetAddress(to[i]);
					System.out.println("To address  ::" + toAddress[i]);
				}

				for (int i = 0; i < toAddress.length; i++) {
					message.addRecipient(Message.RecipientType.BCC, toAddress[i]);
				}
				message.setSubject(email.getSubject());
				message.setText(email.getMessage());
				/*
				 * Transport transport = session.getTransport("smtp");
				 * transport.connect("smtp.gmail.com", 587, Email_Id, password);
				 * transport.sendMessage(message, message.getAllRecipients());
				 * transport.close();
				 */
				Transport.send(message);
				final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
				final LocalDateTime now = LocalDateTime.now();
				for (int i = 0; i < to.length; i++) {

					UserNotification userNotification = new UserNotification();
					userNotification.setNotificationId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
					userNotification.setEmail(to[i]);
					userNotification.setEmailFrom(email.getEmailFrom());
					userNotification.setSubject(email.getSubject());
					userNotification.setMessage(email.getMessage());
					userNotification.setDate(dtf.format(now));
					userNotification.setTime(dtfTime.format(now));
					userService.saveNotification(userNotification);

				}
				email.setId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
				email.setDate(dtf.format(now));
				email.setTime(dtfTime.format(now));
				userService.saveEmail(email);
				System.out.println("Mail sent successfully");
			} catch (MessagingException ex) {
				// Logger.getLogger(emailHtmlTemp.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			} catch (Exception ae) {
				ae.printStackTrace();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/*
	 * @GetMapping("/getCountries") public List<CountryDetails> getCountryDetails()
	 * { String[] locales = Locale.getISOCountries(); CountryDetails countryDetails
	 * = null; List<CountryDetails> countryList = new ArrayList<CountryDetails>();
	 * for (String countryCode : locales) { Locale locale = new Locale("",
	 * countryCode); PhoneNumberUtil phoneNumberUtil =
	 * PhoneNumberUtil.getInstance(); int extensionNumber =
	 * phoneNumberUtil.getCountryCodeForRegion(locale.getCountry()); countryDetails
	 * = new CountryDetails(); countryDetails.setCountryCode(locale.getCountry());
	 * countryDetails.setCountryName(locale.getDisplayCountry()); //
	 * countryDetails.setExtensionNumber(extensionNumber);
	 * countryList.add(countryDetails); } return countryList; }
	 */

	@GetMapping("/getCountries/{countryName}")
	public Integer getExtnBasedOnCountryName(@PathVariable String countryName) {
		return countryDetails.get(countryName);
	}

	/*
	 * @DeleteMapping(value = "/{username}")
	 * 
	 * @PreAuthorize("hasRole('ROLE_ADMIN')") public String delete(@PathVariable
	 * String username) { userService.deleteUser(username); return username; }
	 */

	@GetMapping(value = "/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public UserDataDTO search(@PathVariable String username) {
		return modelMapper.map(userService.search(username), UserDataDTO.class);
	}

	@GetMapping(value = "/me")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	public UserDataDTO whoami(HttpServletRequest req) {
		return modelMapper.map(userService.whoami(req), UserDataDTO.class);
	}

	@GetMapping("/refresh")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	public String refresh(HttpServletRequest req) {
		return userService.refresh(req.getRemoteUser());
	}

	@GetMapping("/isUser/{userName}")
	public void isUserExist(@PathVariable String userName) throws Exception {

		User user = userService.findByUsername(userName);
		if (user != null) {
			throw new Exception("User name is already exist");
		}

	}

	@PostMapping("submitContactUsForm")
	public void contactUs(@RequestBody ContactUs contactUs) throws UnsupportedEncodingException {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Email_Id, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Email_Id, "AI3O"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email_Id));
			// message.setRecipients(Message.RecipientType.BCC,
			// InternetAddress.parse("nji@ai3o.com"));
			message.setSubject("User Query..!!(Contact Us)");
			message.setText("From " + "\n" + contactUs.getFirstName().substring(0, 1).toUpperCase()
					+ contactUs.getFirstName().substring(1) + " " + contactUs.getLastName() + "," + "\n" + "\n"
					+ "Subject : " + contactUs.getSubject() + "\n" + "\n" + "Message : " + contactUs.getMessage() + "\n"
					+ "\n" + "Thank You," + "\n" + contactUs.getFirstName().substring(0, 1).toUpperCase()
					+ contactUs.getFirstName().substring(1) + "\n" + contactUs.getEmail());
			System.out.println("********************* Before submission *********************");
			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
			final LocalDateTime now = LocalDateTime.now();
			Notification notification = new Notification();
			notification.setNotificationId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
			notification.setEmail(contactUs.getEmail());
			notification.setSubject(contactUs.getSubject());
			notification.setMessage(contactUs.getMessage());
			notification.setDate(dtf.format(now));
			notification.setTime(dtfTime.format(now));
			userService.saveQuery(notification);
			Transport.send(message);
			System.out.println("********************* form submitted successfully *********************");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/reply")
	public void reply(@RequestBody ContactUs contactUs) throws UnsupportedEncodingException {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Email_Id, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Email_Id, "AI3O"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(contactUs.getEmail()));
			// message.setRecipients(Message.RecipientType.BCC,
			// InternetAddress.parse("nji@ai3o.com"));
			message.setSubject("Re: " + contactUs.getSubject());
			message.setText(contactUs.getReplymsg());
			System.out.println("********************* Before submission *********************");
			Transport.send(message);
			Email email = new Email();
			email.setEmailFrom(Email_Id);
			email.setMessage(contactUs.getReplymsg());
			email.setSubject("Re: " + contactUs.getSubject());
			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
			final LocalDateTime now = LocalDateTime.now();
			email.setDate(dtf.format(now));
			email.setTime(dtfTime.format(now));
			email.setId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
			String email1[] = new String[1];
			;
			for (int i = 0; i < email1.length; i++) {
				email1[i] = contactUs.getEmail();
			}

			email.setEmails(email1);
			userService.saveEmail(email);

			System.out.println("********************* reply submitted successfully *********************");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/readNotification/{notificationId}")
	public Notification readNotification(@PathVariable long notificationId) {
		System.out.println("notification id is ::" + notificationId);
		return userService.readNotification(notificationId);
	}

	@GetMapping("/readUserNotification/{notificationId}")
	public UserNotification readUserNotification(@PathVariable long notificationId) {
		System.out.println("notification id is ::" + notificationId);
		return userService.readUserNotification(notificationId);
	}

	@GetMapping("/readSentMail/{Id}")
	public Email readSentMail(@PathVariable long Id) {
		System.out.println("email id is ::" + Id);
		return userService.readSentMail(Id);
	}

	@DeleteMapping("/deleteNotification/{notificationId}")
	public String deleteNotification(@PathVariable long notificationId) {
		System.out.println("notification id is ::" + notificationId);
		return userService.deleteNotification(notificationId);
	}

	@DeleteMapping("/deleteSentMail/{Id}")
	public String deleteSentMail(@PathVariable long Id) {
		System.out.println("notification id is ::" + Id);
		return userService.deleteSentMail(Id);
	}

	@DeleteMapping("/deleteUserNotification/{notificationId}")
	public String deleteUserNotification(@PathVariable long notificationId) {
		System.out.println("notification id is ::" + notificationId);
		return userService.deleteUserNotification(notificationId);
	}

	@DeleteMapping("/deleteAllNotifications")
	public String deleteAllNotifications() {
		return userService.deleteAllNotifications();
	}

	@GetMapping("/getNewNotifications")
	public List<Notification> getNewNotifications() {
		List<Notification> notifications = userService.getNotifications();
		if (notifications != null) {
			notifications.sort(Comparator.comparing(Notification::getNotificationId).reversed());
			return notifications;
		}
		return null;
	}

	@GetMapping("/getNewNotifications/{email}")
	public List<Notification> getNewNotifications(@PathVariable String email) {
		List<Notification> notifications = userService.getNotifications();
		if (notifications != null) {
			notifications.sort(Comparator.comparing(Notification::getNotificationId).reversed());
			return notifications;
		}
		return null;
	}

	@GetMapping("/getNotifications")
	public List<Notification> getNotifications() {
		return userService.getNotifications();
	}

	@GetMapping("/getNotificationCount")
	public long getNotificationCount() {
		return userService.getNotificationCount();
	}

	@GetMapping("/emailExist/{emailId}")
	public void isMailExist(@PathVariable String emailId) throws Exception {

		User user1 = userService.findByEmail(emailId);
		if (user1 != null) {
			throw new Exception("User email is already exist");
		}

	}
}
