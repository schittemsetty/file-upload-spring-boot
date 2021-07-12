package com.test.backendapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.backendapi.document.Color;
import com.test.backendapi.service.BaseService;

@RestController
@RequestMapping("/color")
public class ColorController {

	@Autowired
	private BaseService baseService;

	@GetMapping("/getAllColors")
	public List<Color> getAllColors() {

		return baseService.getAllColors();
	}
}
