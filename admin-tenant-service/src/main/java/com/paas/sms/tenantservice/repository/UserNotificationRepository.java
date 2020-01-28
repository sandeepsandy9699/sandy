package com.paas.sms.tenantservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.paas.sms.tenantservice.document.UserNotification;

@Repository
public interface UserNotificationRepository extends MongoRepository<UserNotification, Long> {

	List<UserNotification> findByEmail(String email);

}
