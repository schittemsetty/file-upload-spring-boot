package com.test.backendapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.backendapi.document.Color;

public interface ColorRepository extends MongoRepository<Color, Long> {
	
	Color findByColor(String Color);

}
