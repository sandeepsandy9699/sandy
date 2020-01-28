package com.paas.sms.tenantservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.paas.sms.tenantservice.document.SequenceId;

public interface SequenceRepository {// extends MongoRepository<SequenceId, String>{

	long getNextSequenceId(String key);

}
