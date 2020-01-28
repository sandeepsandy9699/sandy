package com.paas.sms.tenantservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.paas.sms.tenantservice.model.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {

}
