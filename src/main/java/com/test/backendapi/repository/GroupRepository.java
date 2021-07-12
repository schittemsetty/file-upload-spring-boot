package com.test.backendapi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.backendapi.document.Group;

public interface GroupRepository extends MongoRepository<Group, Long> {

	Group findByGroup(String group);

	List<Group> findAllByColor(String color, Pageable pageable);
	
	Group findByGroupAndUser(String group, String user);
}
