package com.paas.license.repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.paas.license.document.UserLicense;

public interface UserLicenseRepository extends MongoRepository<UserLicense, String> {

}
