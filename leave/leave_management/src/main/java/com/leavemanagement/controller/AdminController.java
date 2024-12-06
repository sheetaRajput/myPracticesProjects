package com.leavemanagement.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.ApplyLeave;
import com.leavemanagement.entity.Department;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.LeaveInfoEmployee;
import com.leavemanagement.entity.LeaveInformation;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.service.DeptService;
import com.leavemanagement.service.EmpService;
import com.leavemanagement.service.LeaveEmployeeService;
import com.leavemanagement.service.LeaveService;

@RestController
@RequestMapping("api/v1/anviam/admin")
public class AdminController {

	@Autowired
	private DeptService deptService;

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private EmpService empService;

	@Autowired
	private LeaveEmployeeService leService;

	/**
	 * @RequestBody Department
	 * @return finalResponse
	 */
	@PostMapping("/dept/_create")
	public ResponseEntity<FinalResponse> createDept(@RequestBody Department department) {
		return deptService.createDept(department);
	}

	/**
	 *
	 * @return list of Department
	 */
	@GetMapping("/dept/_getAll")
	public ResponseEntity<Page<Department>> getAllDepartment(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "departmentName") String field) {
		return deptService.getAllDepartment(offSet, pageSize, field);
	}

	/**
	 * @RequestBody Employee
	 * @return finalResponse
	 */
	@PostMapping("/emp/_create")
	public ResponseEntity<FinalResponse> createEmp(@RequestBody Employee employee) {
		return empService.createEmp(employee);
	}

	/**
	 * @PathVariable id
	 * @return if Id is exists it returns the employee data
	 */
	@GetMapping("emp/{id}")
	public ResponseEntity<FinalResponse> fetchDataByEmpId(@PathVariable("id") Long id) throws LeaveNotFoundException {
		return empService.fetchDataByEmpId(id);
	}

	/**
	 *
	 * @return list of Employee
	 */
	@GetMapping("/employees")
	public ResponseEntity<Page<Employee>> fetchDataByList(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field", defaultValue = "name") String field) {
		return empService.fetchDataByList(offSet, pageSize, field);
	}

	/**
	 * @PathVariable empId
	 * @return finalResponse with empId leave is exists or not
	 */
	@GetMapping("emp/leave/{empId}")
	public ResponseEntity<FinalResponse> fetchLeaveByEmpId(@PathVariable("empId") int empId)
			throws LeaveNotFoundException {
		return leaveService.getLeaveByEmpId(empId);
	}

	/**
	 * @Param field
	 * @return list of leave with paging and sorting
	 */
	@GetMapping("/leave/getAll")
	public ResponseEntity<Page<ApplyLeave>> fetchLeaveByList(
			@RequestParam(value = "offSet", defaultValue = "0") int offSet,
			@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
			@RequestParam(value = "field") String field) {
		return leaveService.fetchLeaveByList(offSet, pageSize, field);
	}

	/**
	 * @PathVariable id
	 * @return leave with id
	 */
//	@GetMapping("/leave/{leaveId}")
//	public ResponseEntity<FinalResponse> fetchLeaveById(@PathVariable("leaveId") Long leaveId)
//			throws LeaveNotFoundException {
//		return leaveService.fetchLeaveById(leaveId);
//	}

	/**
	 * @Path leave Id
	 * @path action - approved or reject
	 * @return finalResponse with leave status changes
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/leave/{leaveId}/{action}")
	public ResponseEntity<FinalResponse> getLeaveStatusUpdate(@PathVariable("action") String action,
			@PathVariable("leaveId") Long leaveId) throws UnsupportedEncodingException, MessagingException {
		return leaveService.getLeaveStatusUpdate(action, leaveId);
	}

	/**
	 * @path action - delete , block or unblock
	 * @Path employee Id autogenerate id
	 * @return finalResponse with Employee
	 */
	@GetMapping("/manage-users/{action}/{id}")
	private ResponseEntity<FinalResponse> manageUsers(@PathVariable("action") String action,
			@PathVariable("id") int id) {
		return empService.manageUsers(action, id);
	}

	/**
	 * @return list with Employee
	 */
	@GetMapping("/leaveDetails/save")
	public ResponseEntity<List<Employee>> saveLeaveForEmployees() {
		return leService.saveLeaveForEmployees();
	}

	/**
	 * @RequestBody LeaveInfoEmployee
	 * @return finalResponse with LeaveInfoEmployee
	 */
	@PostMapping("/leaveDetails/save/employee")
	public FinalResponse saveLeaveForFalseEmployees(@RequestBody LeaveInfoEmployee employee) {
		return leService.saveLeaveInfo(employee);
	}

	/**
	 * @RequestParam LeaveInformation
	 * @return LeaveInformation
	 */
	@PostMapping("/employees/leave/save")
	public LeaveInformation saveLeaveInformation(LeaveInformation obj) {
		LeaveInformation saveLeaveDetails = leaveService.saveLeaveDetails(obj);
		return saveLeaveDetails;
	}
}
