package com.leavemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.Department;
import com.leavemanagement.service.DeptService;

@RestController
@RequestMapping("api/v1/dept")
public class DeptController {

	@Autowired
	private DeptService deptService;

	/**
	 *
	 * @return list of Department
	 */
	@GetMapping("/_getAll")
	public ResponseEntity<Page<Department>> getAllDepartment(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "departmentName") String field) {
		return deptService.getAllDepartment(offSet, pageSize, field);
	}

}
