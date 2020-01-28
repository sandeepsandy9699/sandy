package com.paas.license.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paas.license.document.License;
import com.paas.license.document.UserLicense;
import com.paas.license.dto.ProductDTO;
import com.paas.license.repository.LicenseRepository;
import com.paas.license.repository.UserLicenseRepository;

@Service
public class LicenseServiceImpl implements LicenseService {

	@Autowired
	LicenseRepository licenseRepository;

	@Autowired
	UserLicenseRepository userLicenseRepository;

	public License activateLicense(License license) {

		License result = licenseRepository.save(license);
		if (result != null) {
			return result;
		}
		return null;

	}

	@Override
	public License findByUsername(String username) {
		License license = licenseRepository.findByUsername(username);
		if (license != null) {
			return license;
		}
		return null;
	}

	@Override
	public Set<ProductDTO> trailfindByUsername(String username) {
		License license = licenseRepository.findByUsername(username);
		Set<ProductDTO> trialProducts = null;
		if (license != null) {
			Set<ProductDTO> products = license.getUniqueProducts();
			trialProducts = new HashSet<ProductDTO>();
			if (products != null) {
				for (ProductDTO prd : products) {
					if (prd.isTrial())
						trialProducts.add(prd);
				}

			}
			return trialProducts;
		}
		// throw new CustomException("No license activated for this user :" + emailId,
		// HttpStatus.EXPECTATION_FAILED);

		return null;

	}

	@Override
	public Set<ProductDTO> subscribedfindByUsername(String username) {
		License license = licenseRepository.findByUsername(username);
		Set<ProductDTO> subscribedProducts = null;
		if (license != null) {
			Set<ProductDTO> products = license.getUniqueProducts();
			subscribedProducts = new HashSet<ProductDTO>();
			if (products != null) {
				for (ProductDTO prd : products) {
					if (!prd.isTrial())
						subscribedProducts.add(prd);
				}

			}
		}
		return subscribedProducts;
	}

	@Override
	public List<License> getAllActiveLicenses() {

		return licenseRepository.findAll();
	}

	@Override
	public String deleteLicenseByUsername(License license) {
		licenseRepository.delete(license);
		return null;
	}

	@Override
	public void assignProducts(UserLicense tenantLicense) {

		userLicenseRepository.save(tenantLicense);
	}

	@Override
	public long getTrialCount() {
		List<Set<ProductDTO>> se = licenseRepository.findAll().stream().map(License::getUniqueProducts)
				.collect(Collectors.toList());

		return 0;
	}

}
