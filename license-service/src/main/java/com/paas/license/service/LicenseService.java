package com.paas.license.service;

import java.util.List;
import java.util.Set;

import com.paas.license.document.License;
import com.paas.license.document.UserLicense;
import com.paas.license.dto.ProductDTO;

public interface LicenseService {

	License activateLicense(License license);

	List<License> getAllActiveLicenses();

	Set<ProductDTO> subscribedfindByUsername(String username);

	String deleteLicenseByUsername(License license);

	Set<ProductDTO> trailfindByUsername(String username);

	void assignProducts(UserLicense tenantLicense);

	long getTrialCount();

	License findByUsername(String username);

}
