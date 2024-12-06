package com.leavemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.Configuration;
import com.leavemanagement.service.ConfigurationService;

@RestController
@RequestMapping("api/v1/configuration")
public class ConfigurationController {

	@Autowired
	private ConfigurationService service;

	@PostMapping("/create")
	private Configuration create(@RequestBody Configuration configuration) {
		return service.create(configuration);
	}

	@PostMapping("/update")
	public Long update(@RequestBody Configuration dto) {
		return service.update(dto);
	}

	@GetMapping("/getAllLeaveType")
	public ResponseEntity<Page<Configuration>> getAllLeaveType(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "type") String field) {
		return service.list(offSet, pageSize, field);
	}
	
	@GetMapping("/getValues")
	private String getValue(String value) {
		return service.getValue(value);
	}
}
