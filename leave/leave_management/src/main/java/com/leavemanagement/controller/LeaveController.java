package com.leavemanagement.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leavemanagement.entity.ApplyLeave;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.LeaveInfoEmployee;
import com.leavemanagement.service.LeaveEmployeeService;
import com.leavemanagement.service.LeaveService;

@RestController
@RequestMapping("/api/v1/leave")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;

	@Autowired
	private LeaveEmployeeService service;

	/**
	 * @RequestBody leave
	 * @return FinalResponse with leaveStatus, sendVerification email, DateFormats
	 *         dd-MM-yyyy HH:mm:ss with 24 Hours
	 */
	@PostMapping("/apply-leave")
	private ResponseEntity<FinalResponse> applyLeave(@RequestBody ApplyLeave leave, BindingResult result,
			HttpServletRequest request) {
		return leaveService.applyLeave(leave, getSiteURL(request));
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	/**
	 * @PathVariable empId
	 * @return list if leave with empId
	 */
	@GetMapping("/leaves/{empId}")
	private ResponseEntity<FinalResponse> getLeaveByEmpId(@PathVariable("empId") int empId) {
		return leaveService.getLeaveByEmpId(empId);
	}

	/**
	 * @PathVariable empId
	 * @return list if leave with empId
	 */
	@GetMapping("/checkleavedetails/{empId}")
	private LeaveInfoEmployee getEmployeeLeaveDetails(@PathVariable("empId") int empId) {
		return service.getEmployeeLeaveDetails(empId);
	}

}
