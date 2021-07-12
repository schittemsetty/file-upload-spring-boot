package com.test.backendapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.backendapi.document.Group;
import com.test.backendapi.exception.handler.CustomException;
import com.test.backendapi.service.BaseService;

@RestController
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private BaseService baseService;

	@GetMapping("/getAll")
	public Map<String, Object> getAll(String color, int page, int size) {

		return baseService.getAll(color, page, size);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/create")
	public List<Group> createGroup(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		byte[] bytes = null;
		try {
			bytes = file.getBytes();
		} catch (IOException e) {
			throw new CustomException("Invalid json", 401);
		}
		String json = new String(bytes);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, String>> groups = null;
		try {
			groups = mapper.readValue(json, HashMap.class);
		} catch (Exception e) {
			throw new CustomException("Invalid json", 401);
		} 

		return baseService.createGroup(groups);
	}

}
