package com.sbms.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sbms.models.Sbms;

public interface SbmsRepository extends MongoRepository<Sbms, String>{

	Sbms findByDate(String date);

}
