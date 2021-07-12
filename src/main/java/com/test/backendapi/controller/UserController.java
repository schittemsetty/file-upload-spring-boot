package com.test.backendapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.backendapi.document.User;
import com.test.backendapi.service.BaseService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BaseService baseService;

	@PostMapping("/signIn")
	public ResponseEntity<String> signIn(@RequestBody User user) {

		String token = baseService.signIn(user);
		return ResponseEntity.ok(token);
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {

		baseService.register(user);
		return ResponseEntity.ok("Email created successfully");
	}

}
