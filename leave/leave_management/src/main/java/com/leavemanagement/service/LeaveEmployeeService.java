package com.leavemanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.leavemanagement.controller.AdminController;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.LeaveInfoEmployee;
import com.leavemanagement.entity.LeaveInformation;
import com.leavemanagement.repository.LeaveInfoRepository;

@Service
public class LeaveEmployeeService {

	@Autowired
	private LeaveInfoRepository repo;
	
	@Autowired
	private EmpService empService;
	
	@Autowired(required = true)
	private LeaveService leaveService;
	private final Logger logger= LoggerFactory.getLogger(AdminController.class);

	public FinalResponse saveLeaveInfo(LeaveInfoEmployee obj) {
		logger.info("saveLeaveInfo[START] ");
		FinalResponse finalResponse = new FinalResponse();
		LeaveInfoEmployee save = repo.save(obj);
		finalResponse.setMessage("Success");
		finalResponse.setStatus(true);
		finalResponse.setData(save);
		logger.info(" saveLeaveInfo[END]");
		return finalResponse;
	}

	public List<LeaveInfoEmployee> getAllLeaves() {
		logger.info("getAllLeaves[START-END]");
		return (List<LeaveInfoEmployee>) repo.findAll();
	}

	public LeaveInfoEmployee getEmployeeLeaveDetails(int empId) {
		logger.info("getEmployeeLeaveDetails[START]");
		for (LeaveInfoEmployee obj : getAllLeaves()) {
			if (obj.getEmployeeId() == empId) {
				return obj;
			}
		}
		logger.info("getEmployeeLeaveDetails[END]");
		return null;
	}
	
	public ResponseEntity<List<Employee>> saveLeaveForEmployees(){
		LeaveInformation obj = leaveService.getLeaveDetails();
		List<Employee> listEmp = empService.getAllEmployee();
		try {
		for (Employee emp : listEmp) {
			if (emp.getActive() == false) {

			} else {
				LeaveInfoEmployee leaveForEmp = new LeaveInfoEmployee();
				LeaveInfoEmployee allLeaves = getEmployeeLeaveDetails(emp.getEmpId());
				if (allLeaves == null) {
					leaveForEmp.setEmployeeId(emp.getEmpId());
					leaveForEmp.setCovidLeave(obj.getCovidLeave());
					leaveForEmp.setEarnedLeave(obj.getEarnedLeave());
					leaveForEmp.setInicidentalLeave(obj.getInicidentalLeave());
					leaveForEmp.setLeaveWithoutPay(obj.getLeaveWithoutPay());
					leaveForEmp.setShortLeave(obj.getShortLeave());
					leaveForEmp.setWfh(obj.getWfh());
					saveLeaveInfo(leaveForEmp);
				}
			}
		}
		List<Employee> employees = empService.getAllEmployee();
		return new ResponseEntity<List<Employee>>(employees,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Employee>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
