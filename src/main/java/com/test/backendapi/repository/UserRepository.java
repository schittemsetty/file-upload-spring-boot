package com.test.backendapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.backendapi.document.User;

public interface UserRepository extends MongoRepository<User, Long> {

	User findByEmail(String email);
	
}
