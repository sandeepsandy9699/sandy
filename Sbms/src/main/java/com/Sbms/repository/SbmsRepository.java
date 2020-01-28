package com.Sbms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Sbms.document.Sbms;



public interface SbmsRepository extends MongoRepository<Sbms, String>{

	Sbms findByDate(String date);

}
