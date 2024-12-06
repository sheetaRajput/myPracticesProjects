package com.leavemanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.service.EmpService;

@RestController
@RequestMapping("/api/v1/emp")
public class EmpController {

	@Autowired
	private EmpService empService;
	private final Logger Logger = LoggerFactory.getLogger(EmpController.class);

	/**
	 * @RequestBody employee
	 * @return FinalResponse with employee
	 */
	@PostMapping("/_create")
	public ResponseEntity<FinalResponse> createEmp(@RequestBody Employee employee) {
		Logger.info("inside a save data in EmpController");
		return empService.createEmp(employee);
	}

	/**
	 * @PathVariable id
	 * @return if Id is exists it returns the employee data
	 */
	@GetMapping("personaldetails/{id}")
	public ResponseEntity<FinalResponse> fetchDataByEmpId(@PathVariable("id") Long id) throws LeaveNotFoundException {
		Logger.info("inside a get by id data in EmpController");
		return empService.fetchDataByEmpId(id);
	}

	/**
	 *
	 * @return list of Employee
	 */
	@GetMapping("/employee")
	public ResponseEntity<Page<Employee>> fetchDataByList(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "name") String field) {
		return empService.fetchDataByList(offSet, pageSize, field);
	}
	
	/**
	 * @path empName
	 * @return finalResponse
	 */
	@GetMapping("/getByEmployeeName/{empName}")
	private ResponseEntity<FinalResponse> getByEmployeeName(@PathVariable("empName") String empName){
		return empService.getByEmployeeName(empName);
	}
}
