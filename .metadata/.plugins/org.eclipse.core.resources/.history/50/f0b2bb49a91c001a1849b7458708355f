package com.paas.sms.product.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.paas.sms.product.exceptions.CustomException;
import com.paas.sms.product.model.Product;
import com.paas.sms.product.repository.ProductRepository;
import com.paas.sms.product.repository.SequenceRepository;
import com.paas.sms.product.service.ProductService;

@RestController
@RequestMapping("/paasproduct")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

	/*
	 * long product_id; private static AtomicInteger ID_GENERATOR = new
	 * AtomicInteger(1001);
	 */

	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private ProductRepository productRepository;

	/*
	 * @Autowired private UserProductRepository userProductRepository;
	 */

	@Autowired
	ProductService productService;

	private static final String HOSTING_SEQ_KEY = "productId";

	RestTemplate restTemplate;

	String cartUri = "http://localhost:2170/paascart/cart/";

	String licenseUri = "http://localhost:2175/paaslicense/license/";
	String cartProductUri = "http://localhost:2170/paascart/cart/update";

	@PutMapping("/update")
	public Product update(@RequestBody Product p) {

		List<String> prds = productRepository.findAll().stream().map(Product::getProductName)
				.collect(Collectors.toList());

		Optional<Product> opt = productRepository.findById(p.getProductId());
		Product prd = null;
		if (opt.isPresent()) {
			prd = opt.get();
			if (!prd.getProductName().equalsIgnoreCase(p.getProductName())) {
				if (prds.contains(p.getProductName())) {
					throw new CustomException("Product name is already exist!!", HttpStatus.CONFLICT);
				}

			}
		}

		String pname = p.getProductName();

		p.setSearchProductName(pname.toLowerCase());

		Product p1 = productRepository.save(p);
		if (p1 != null) {
			return p1;
		} else {
			Product p2 = new Product();
			p2.setMessage("Product Not saved ");
			return p2;
		}
	}

	@PostMapping("/addProduct")
	public Product saveProduct(@RequestBody Product product) throws Exception {

		List<Product> products = productRepository.findAll();
		if (!CollectionUtils.isEmpty(products)) {
			for (Product prd : products) {
				if (prd.getProductName().equalsIgnoreCase(product.getProductName())) {
					throw new Exception("Product name already exist in product list..!!");
				}
			}
		}
		product.setProductId(sequenceRepository.getNextSequenceId(HOSTING_SEQ_KEY));
		String pname = product.getProductName();

		product.setSearchProductName(pname.toLowerCase());
		Product p = productRepository.save(product);
		if (p != null) {
			return p;
		}
		final Product p1 = new Product();
		p1.setMessage("Product Not saved ");
		return p1;
	}

	@GetMapping("/getProductCount")
	public long getProductCount() {

		@SuppressWarnings("resource")
		MongoClient mongo = new MongoClient("localhost", 27017);

		@SuppressWarnings("deprecation")
		DB db = mongo.getDB("Products");
		DBCollection col = db.getCollection("Product");
		long productCount = col.find().count();

		return productCount;
	}

	@GetMapping("/findAllProducts")
	public List<Product> getProducts() throws Exception {
		List<Product> products = productRepository.findAll();
		if (CollectionUtils.isEmpty(products)) {
			throw new Exception("No products available");
		}
		return products;
	}

	@GetMapping("/newProducts")
	public List<Product> newProducts() {
		List<Product> products = productRepository.findAll();
		if (products != null) {
			products.sort(Comparator.comparing(Product::getProductId).reversed());
			return products;
		} else {
			return null;
		}
	}

	@GetMapping("/getDate")
	private String getDate() {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		final LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	@GetMapping("/search/{productName}")
	public List<Product> getProductsByName(@PathVariable String productName) {
		String searchProductName = productName.toLowerCase();
		System.out.println("Product name is ::" + searchProductName);
		List<Product> products = productRepository.findBySearchProductNameLike(searchProductName);
		System.out.println("prod list ::" + products);
		return products;
	}

	@GetMapping("/findById/{id}")
	public Optional<Product> getProduct(@PathVariable long id) {
		return productRepository.findById(id);

	}

	@DeleteMapping("/delete/{id}")
	public String deleteProduct(@PathVariable long id) {
		productRepository.deleteById(id);
		// suiteRepository.findById(id)
		return "Product deleted with id: " + id;
	}

	@GetMapping("/findAllUnSubscribedProducts/{username}")
	public List<Product> getunSubscribedProducts(@PathVariable String username, HttpServletRequest request)
			throws Exception {
		List<Product> dbProducts = productRepository.findAll();
		

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		headers.set("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		restTemplate = new RestTemplate();

		List<Long> licensedProductIds = restTemplate
				.exchange(licenseUri + "findAllProductIds/" + username, HttpMethod.GET, entity, List.class).getBody();

		List<Product> updatedProducts = new ArrayList<Product>();
		if (licensedProductIds != null) {
			for (Product prd : dbProducts) {
				int id = (int) prd.getProductId();
				if (!(licensedProductIds.contains(id))) {
					updatedProducts.add(prd);
				}
			}
			return updatedProducts;
		} else {
			throw new Exception("No products available");
		}

	}
}