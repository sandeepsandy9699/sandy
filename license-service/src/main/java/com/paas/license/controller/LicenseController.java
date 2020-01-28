package com.paas.license.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.paas.license.document.License;
import com.paas.license.document.UserLicense;
import com.paas.license.dto.ProductDTO;

import com.paas.license.repository.UserLicenseRepository;
import com.paas.license.service.LicenseService;

@RestController
@RequestMapping("/license")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LicenseController {

	@Autowired
	LicenseService licenseService;

	RestTemplate restTemplate;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	UserLicenseRepository userLicenseRepository;

	String uri = "http://localhost:2170/paascart/cart/";

	MongoClient mongo = new MongoClient("localhost", 27017);

	@PostMapping("/activateLicense/{username}/{role}/{clientmaster}/{siteadmin}")
	public License activateSubscribedLicense(@RequestBody Set<ProductDTO> prds, @PathVariable String username,
			@PathVariable String role, @PathVariable String clientmaster, @PathVariable String siteadmin,
			HttpServletRequest request)// @PathVariable String paymentMode,@PathVariable
			// String transactionNumber,
			throws Exception {

		List<Long> prdIds = new ArrayList<>();

		for (ProductDTO prd : prds) {

			prdIds.add(prd.getProductId());
		}
		System.out.println("siteadmin " + siteadmin);
		// String siteadmin = user.getUsername();

		if (!clientmaster.equalsIgnoreCase("null")) {
			setLicense(prds, clientmaster, "ROLE_CLIENT_MASTER", request);
		}
		if (!siteadmin.equalsIgnoreCase("null")) {
			setLicense(prds, siteadmin, "ROLE_SITE_ADMIN", request);
		}
		// String clientmaster = user.getUsername();

		License license = setLicense(prds, username, role, request);
		if (license != null) {
			deleteProductFromCart(request, prdIds, username, license, role);
		}
		return license;
	}

	private License setLicense(Set<ProductDTO> products, String username, String role, HttpServletRequest request)
			throws Exception {
		int days = 0;
		Set<ProductDTO> newprods = new HashSet<ProductDTO>();
		Calendar c = Calendar.getInstance();

		for (ProductDTO prd : products) {
			prd.setStartDate(setStartDate());
			if (prd.isTrial()) {
				days = Integer.parseInt(prd.getTrialDuration());
			} else {
				days = Integer.parseInt(prd.getProductDuration());
			}
			c.add(Calendar.DAY_OF_MONTH, days);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			prd.setEndDate(sdf.format(c.getTime()));
			prd.setPaymentMode("Online");
			String transactionNumber = new DecimalFormat("000000").format(new Random().nextInt(999999));
			prd.setTransactionNumber(transactionNumber);
			newprods.add(prd);

		}
		License license = licenseService.findByUsername(username);
		if (license != null) {
			Set<ProductDTO> dbProducts = license.getUniqueProducts();
			List<Long> prdIds = dbProducts.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
			if (dbProducts != null) {
				for (ProductDTO prd : products)
					for (ProductDTO dbprd : dbProducts){
					if (dbprd.getProductId()==prd.getProductId() && !dbprd.isTrial()) {// 
						if (role.equalsIgnoreCase("ROLE_USER") || role.equalsIgnoreCase("ROLE_CLIENT_MASTER")) {
							throw new Exception(
									"Product : " + prd.getProductName() + " already subscribed by " + username);
						}
						throw new Exception(
								"Product : " + prd.getProductName() + " already subscribed by you or your superior");
					}
				}
				newprods.addAll(dbProducts);
			}
			license = setLicenseProducts(license, newprods, username);
		} else {
			license = new License();
			license = setLicenseProducts(license, newprods, username);
		}

		return license;
	}

	private License setLicenseProducts(License license, Set<ProductDTO> products, String username) {

		license.setUniqueProducts(products);
		license.setUsername(username);
		license = licenseService.activateLicense(license);
		return license;
	}

	@PostMapping("/addProduct/{username}/{role}/{paymentMode}/{transactionNumber}")
	public License addProduct(@RequestBody Set<ProductDTO> prds, @PathVariable String username,
			@PathVariable String role, HttpServletRequest request, @PathVariable String paymentMode,
			@PathVariable String transactionNumber) throws Exception {
		/*
		 * String regex="[0-9]"; if(!transactionNumber.matches(regex)) { throw new
		 * Exception("Invalid transaction number"); }
		 */
		Set<ProductDTO> products = new HashSet<>();
		List<Long> prdIds = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		for (ProductDTO prd : prds) {
			prd.setStartDate(setStartDate());
			int days = Integer.parseInt(prd.getProductDuration());
			c.add(Calendar.DAY_OF_MONTH, days);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			prd.setEndDate(sdf.format(c.getTime()));
			prd.setPaymentMode(paymentMode);
			prd.setTransactionNumber(transactionNumber);
			products.add(prd);
			prdIds.add(prd.getProductId());
		}

		License license = licenseService.findByUsername(username);
		if (license != null) {
			Set<ProductDTO> dbProducts = license.getUniqueProducts();
			if (dbProducts != null) {
				for (ProductDTO dbprd : dbProducts) {
					if (prdIds.contains(dbprd.getProductId()) && !dbprd.isTrial()) {
						throw new Exception("Product : " + dbprd.getProductName() + " already subscribed");
					}
				}
				products.addAll(dbProducts);
			}
			license = setLicenseProducts(license, products, username);
		} else {
			license = new License();

			license = setLicenseProducts(license, products, username);
		}
		if (license != null) {
			deleteProductFromCart(request, prdIds, username, license, role);
		}
		return license;
	}

	@PutMapping("/deActivateLicense/{username}/{productId}")
	public License deActivateLicense(@PathVariable String username, @PathVariable long productId) {
		License license = licenseService.findByUsername(username);
		if (license != null) {
			Set<ProductDTO> prods = license.getUniqueProducts();
			if (prods != null) {
				for (ProductDTO prod : prods) {
					if (prod.getProductId() == productId) {
						prods.remove(prod);
						license.setUsername(username);
						license.setUniqueProducts(prods);
						licenseService.activateLicense(license);
						return license;
					}
				}
			}
		}
		return null;
	}

	@GetMapping("/{username}")
	public License getLicenseBasedOnUsername(@PathVariable String username, HttpServletResponse res) {
		License license = licenseService.findByUsername(username);
		if (license != null) {
			Set<ProductDTO> prds = license.getUniqueProducts();
			prds = (prds != null) ? prds : Collections.emptySet();
			Set<ProductDTO> subscribedProducts = prds.stream().filter(prd -> !prd.isTrial())
					.collect(Collectors.toSet());
			license.setUniqueProducts(subscribedProducts);
			license.setUsername(username);
			return license;
		}
		// throw new Exception("No license activated for this user :" + emailId +
		// HttpStatus.EXPECTATION_FAILED);
		// throw new CustomException("No license activated for this user :" + emailId,
		res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		// res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED, "No license
		// activated for this user :" + emailId);
		return null;
	}

	@SuppressWarnings("unused")
	@GetMapping("/getLicense/{username}")
	public String getLicense(@PathVariable String username, HttpServletResponse res) {
		/*
		 * License license = licenseService.findByUsername(username); ProductDTO product
		 * = new ProductDTO(); List<ProductDTO> prodList = new ArrayList<ProductDTO>();
		 * if (license != null) { Set<ProductDTO> prods = license.getUniqueProducts();
		 * for (ProductDTO prod : prods) { product.setProductId(prod.getProductId());
		 * product.setProductName(prod.getProductName());
		 * product.setTrial(prod.isTrial());
		 * product.setAssignedBy(prod.getAssignedBy());
		 * product.setSubscribedBy(prod.getSubscribedBy()); prodList.add(product); }
		 * return prodList; } res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		 * return Collections.emptyList();
		 */

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("license");
		DBCollection col = db.getCollection("License");
		List<String> items = new ArrayList<>();
		BasicDBObject where = new BasicDBObject();
		where.put("_id", username);
		BasicDBObject fields = new BasicDBObject();
		fields.put("uniqueProducts.productImage", 0);
		fields.put("uniqueProducts.productIcon", 0);
		fields.put("uniqueProducts.productId", 0);

		Iterator<DBObject> cursor = col.find(where, fields).iterator();
		try {
			while (cursor.hasNext()) {
				items.add(((BasicDBObject) cursor.next()).toJson());
			}
		} finally {
			((DBCursor) cursor).close();
		}
		if (items != null) {
			return "[" + String.join(", ", items) + "]";
		} else {
			return items.toString();
		}

	}

	@GetMapping("/trial/{username}")
	public License getTrialLicenseBasedOnEmail(@PathVariable String username) {
		License license = licenseService.findByUsername(username);
		Set<ProductDTO> prds = (license != null) ? license.getUniqueProducts() : Collections.emptySet();
		Set<ProductDTO> trialProducts = prds.stream().filter(prd -> prd.isTrial()).collect(Collectors.toSet());
		license.setUniqueProducts(trialProducts);
		license.setUsername(username);
		return license;
	}

	/*
	 * @PutMapping("/update/{newMail}/{oldMail}") public void update(@PathVariable
	 * String newMail, @PathVariable String oldMail) {
	 * System.out.println("*********Entered into update method in licence**********"
	 * ); License license = licenseService.findByEmail(oldMail); if (license !=
	 * null) {
	 * 
	 * license.setEmail(newMail);
	 * license.setUniqueProducts(license.getUniqueProducts());
	 * licenseService.activateLicense(license); License oldLicense =
	 * licenseService.findByEmail(oldMail); if (oldLicense != null) {
	 * licenseService.deleteLicenseByEmail(oldLicense); } }
	 * System.out.println("*********Exit update method in licence**********"); }
	 */

	@DeleteMapping("/deleteProduct/{productId}")
	public String deleteProductUsingProductId(@PathVariable long productId) throws Exception {
		List<License> licenseList = licenseService.getAllActiveLicenses();
		if (licenseList != null) {
			for (License license : licenseList) {
				String username = license.getUsername();
				Set<ProductDTO> prds = license.getUniqueProducts();
				if (!CollectionUtils.isEmpty(prds)) {
					Set<ProductDTO> updatedProducts = prds.stream().filter(prd -> productId != prd.getProductId())
							.collect(Collectors.toSet());
					setLicenseProducts(license, updatedProducts, username);
				}
			}
		}
		return "Product remved from cart";
	}

	@GetMapping("/findAllProducts/{username}")
	public Set<ProductDTO> getAllLicensedProducts(@PathVariable String username) {
		License license = licenseService.findByUsername(username);
		return license.getUniqueProducts();
	}

	@PostMapping("/exchangeProduct/{productId}/{username}")
	public String exchangeProduct(@PathVariable long productId, @PathVariable String username,
			@RequestBody ProductDTO destProd) throws Exception {
		License license = licenseService.findByUsername(username);
		Set<ProductDTO> list = license.getUniqueProducts();
		List<Long> prdIds = list.stream().map(ProductDTO::getProductId).collect(Collectors.toList());
		if (prdIds.contains(destProd.getProductId())) {
			throw new Exception(destProd.getProductName() + " already subscribed");
		}
		Calendar c = Calendar.getInstance();
		for (ProductDTO prod : list) {
			if (prod.getProductId() == productId) {
				if (destProd.getProductPrice() >= prod.getProductPrice()) {
					Set<ProductDTO> updatedPrds = list.stream().filter(prd -> prd.getProductId() != productId)
							.collect(Collectors.toSet());
					System.out.println("start date :: " + setStartDate());
					// destProd.setPaymentMode("Exchange");
					// String transactionnumber = new DecimalFormat("000000").format(new
					// Random().nextInt(999999));
					// destProd.setTransactionNumber(transactionnumber);
					destProd.setStartDate(setStartDate());
					int days = Integer.parseInt(destProd.getProductDuration());
					c.add(Calendar.DAY_OF_MONTH, days);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					destProd.setEndDate(sdf.format(c.getTime()));
					System.out.println("Product :: " + destProd);
					updatedPrds.add(destProd);
					license.setUniqueProducts(updatedPrds);
					licenseService.activateLicense(license);
					return "Product exchanged for user " + license.getUsername();
				} else {
					throw new Exception(
							destProd.getProductName() + " price should not be less than " + prod.getProductName());
				}
			}
		}
		return null;
	}

	@GetMapping("/findAllProductIds/{username}")
	public List<Long> getAllLicensedProductIds(@PathVariable String username) {
		License license = licenseService.findByUsername(username);

		Set<ProductDTO> prds = (license != null) ? license.getUniqueProducts() : null;
		if (prds != null) {

			return prds.stream().map(ProductDTO::getProductId).collect(Collectors.toList());

		}
		return Collections.emptyList();
	}

	private String setStartDate() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		final LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	@PostMapping("/trial/{username}")
	public License activateTrialLicense(@RequestBody Set<ProductDTO> prds, @PathVariable String username,
			HttpServletResponse res) throws Exception {
		Set<ProductDTO> products = new HashSet<>();
		List<Long> prdIds = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		for (ProductDTO prd : prds) {
			prd.setStartDate(setStartDate());
			int days = Integer.parseInt(prd.getProductDuration());
			c.add(Calendar.DAY_OF_MONTH, days);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			prd.setEndDate(sdf.format(c.getTime()));
			products.add(prd);
			prdIds.add(prd.getProductId());
		}

		Set<ProductDTO> trialProducts = licenseService.trailfindByUsername(username);
		/*
		 * if (trialProducts == null || trialProducts.size() <= 0) {
		 * res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED); }
		 */
		License license = licenseService.findByUsername(username);
		Set<ProductDTO> subscribedProducts = licenseService.subscribedfindByUsername(username);
		if (license != null) {
			if (trialProducts != null) {
				for (ProductDTO dbprd : trialProducts) {
					if (prdIds.contains(dbprd.getProductId())) {
						throw new Exception("Product : " + dbprd.getProductName() + " already in trial period");
					}
				}
				products.addAll(trialProducts);
				products.addAll(subscribedProducts);

				license = setLicenseProducts(license, products, username);

			}

		} else {
			license = new License();

			license = setLicenseProducts(license, products, username);
		}
		return license;
	}

	@SuppressWarnings("null")
	@PostMapping("/assignProduct/{username}")
	public void assignProducts(@RequestBody ProductDTO prod, @PathVariable String username, ServletResponse response)
			throws Exception {

		HttpServletResponse servletResponse = (HttpServletResponse) response;
		License license = licenseService.findByUsername(username);
		boolean flag = false;
		Set<ProductDTO> prods = null;
		if (license != null) {
			prods = license.getUniqueProducts();
			List<Long> prdIds = prods.stream().map(ProductDTO::getProductId).collect(Collectors.toList());

			if (prdIds.contains(prod.getProductId())) {
				// throw new CustomException("This product already exists for this user",
				// HttpStatus.CONFLICT);
				servletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
				flag = true;
				// throw new Exception("This product already exists for this user");
			}
			if (!flag) {
				license.setUsername(username);
				prods.add(prod);
				license.setUniqueProducts(prods);
				licenseService.activateLicense(license);
			}
		} else {
			license = new License();
			prods = new HashSet<>();
			license.setUsername(username);
			prods.add(prod);
			license.setUniqueProducts(prods);
			licenseService.activateLicense(license);
		}
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/getSubTenantProducts")
	public List<ProductDTO> getSubTenantProducts(@PathVariable String username) {
		Optional<UserLicense> tenantLicense = userLicenseRepository.findById(username);
		if (tenantLicense != null) {
			UserLicense tl = tenantLicense.get();
			return (List<ProductDTO>) tl.getUniqueProducts();
		}
		return null;

	}

	@GetMapping("/unSubscribedProducts/{username}")
	public License getUnsubscribedProducts(@PathVariable String username, HttpServletResponse res) {
		Set<ProductDTO> trialProducts = licenseService.trailfindByUsername(username);
		if (trialProducts == null || trialProducts.size() <= 0) {
			// throw new CustomException("No license activated for this user :" + emailId,
			// HttpStatus.EXPECTATION_FAILED);
			res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		}
		Set<ProductDTO> subscribedProducts = licenseService.subscribedfindByUsername(username);
		Set<Long> sprdIds = new HashSet<>();
		if (subscribedProducts != null) {
			for (ProductDTO prd : subscribedProducts) {
				sprdIds.add(prd.getProductId());
			}
		}
		Set<ProductDTO> uprds = trialProducts != null ? trialProducts.stream()
				.filter(prd -> !(sprdIds.contains(prd.getProductId()))).collect(Collectors.toSet()) : null;
		License license = new License();
		license.setUsername(username);
		license.setUniqueProducts(uprds);
		return license;
	}

	@GetMapping("/assignedProducts/{username}")
	public Set<ProductDTO> assignedProducts(@PathVariable String username) {
		License license = licenseService.findByUsername(username);
		Set<ProductDTO> prds = (license != null) ? license.getUniqueProducts() : null;
		if (prds != null) {
			return prds;
		}
		return null;
	}

	@DeleteMapping("/deactivate/{username}")
	public String deactivateLicenseBasedOnUsername(@PathVariable String username) {
		License license = licenseService.findByUsername(username);
		if (license != null) {
			licenseService.deleteLicenseByUsername(license);
			return "License deactivated for :" + username;
		}
		return "License not deactivated";

	}

	@GetMapping("/getTrailCount")
	public long getTriaCount() {
		return licenseService.getTrialCount();
	}

	@GetMapping("/subscribedProductsCount/{username}")
	public long subscribedProductsCount(@PathVariable String username) {

		License license = licenseService.findByUsername(username);

		Set<ProductDTO> prds = (license != null) ? license.getUniqueProducts() : null;
		List<ProductDTO> list = null;
		long Count = 0;
		if (prds != null) {
			list = prds.stream().filter(prd -> !prd.isTrial()).collect(Collectors.toList());
			Count = list.size();
		}
		return Count;
	}

	@PutMapping("/extendLicense/{username}/{productId}/{endDate}")
	public void extendLicense(@PathVariable String username, @PathVariable long productId,
			@PathVariable String endDate) {
		License license = licenseService.findByUsername(username);
		Set<ProductDTO> dbPrds = license.getUniqueProducts();
		System.out.println("enddate...." + endDate);
		// Instant instant = Instant.parse(endDate);
		// LocalDateTime result = LocalDateTime.ofInstant(instant,
		// ZoneId.of(ZoneOffset.UTC.getId()));
		for (ProductDTO prd : dbPrds) {
			if (productId == prd.getProductId()) {
				prd.setEndDate(endDate);
			}
		}
		license.setUniqueProducts(dbPrds);
		licenseService.activateLicense(license);
	}

	private void deleteProductFromCart(HttpServletRequest request, List<Long> prdIds, String emailId, License license,
			String role) {
		if (license != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpSession session = request.getSession();
			String token = (String) session.getAttribute("token");
			headers.set("Authorization", token);
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			restTemplate = new RestTemplate();
			Set<ProductDTO> lprds = license.getUniqueProducts();

			if (lprds != null) {
				for (ProductDTO prd : lprds) {
					long prdId = prd.getProductId();
					if (prdIds.contains(prdId) && !prd.isTrial() && !role.equalsIgnoreCase("ROLE_ADMIN")) {
						restTemplate.exchange(uri + "product/" + emailId + "/" + prdId, HttpMethod.DELETE, entity,
								String.class);
					}

				}
			}
		}
	}
}