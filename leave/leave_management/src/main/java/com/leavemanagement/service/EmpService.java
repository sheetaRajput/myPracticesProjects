package com.leavemanagement.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.leavemanagement.config.CommonConfig;
import com.leavemanagement.controller.EmpController;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.repository.EmpRepo;
import com.leavemanagement.utils.CommonUtils;

@Service
public class EmpService {
	@Autowired
	private EmpRepo empRepo;
	private final Logger LOGGER = LoggerFactory.getLogger(EmpController.class);
	@Autowired
	private CommonConfig config;

	/**
	 * @RequestBody employee
	 * @return FinalResponse with employee
	 */
	public ResponseEntity<FinalResponse> createEmp(Employee employee) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("getCreate[START] input parameters:" + employee.toString());
		try {
			if (CommonUtils.isNullOrEmpty(employee.getId())) {
				FinalResponse validation = validation(employee);

				if (validation.getMessage() != null) {
					return new ResponseEntity<FinalResponse>(validation, HttpStatus.NO_CONTENT);
				} else {
					employee.setActive(Boolean.TRUE);
					employee.setEmpId(employee.getEmpId());
					Employee save = empRepo.save(employee);
					finalResponse.setData(save);
					finalResponse.setMessage(config.getEmpSuccess());
					finalResponse.setStatus(true);
					LOGGER.info("getCreate[END] input parameters:" + employee.toString());
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.CREATED);
				}
			} else {
				FinalResponse validation = validation(employee);
				if (validation.getMessage() != null) {
					return new ResponseEntity<FinalResponse>(validation, HttpStatus.NO_CONTENT);
				} else {
					employee.setActive(Boolean.TRUE);
					employee.setEmpId(employee.getEmpId());
					Employee save = empRepo.save(employee);
					finalResponse.setData(save);
					finalResponse.setMessage(config.getEmpUpdated());
					finalResponse.setStatus(true);
					LOGGER.info("getCreate[END] update input parameters:" + employee.toString());
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(config.getEmployeeCreateExce() + e.getMessage());
			finalResponse.setStatus(Boolean.FALSE);
			finalResponse.setMessage(config.getEmployeeCreateExce());
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private FinalResponse validation(Employee employee) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.info("validation [START] input parameters:" + employee.toString());
		if (CommonUtils.isNullOrEmpty(employee.getName())) {
			finalResponse.setMessage("Name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(employee.getEmail())) {
			finalResponse.setMessage("Email is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(employee.getMobileNumber())) {
			finalResponse.setMessage("Mobile Number is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(employee.getAddress())) {
			finalResponse.setMessage("Address is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(employee.getDateOfBirth())) {
			finalResponse.setMessage("Dob  is null !");
			finalResponse.setStatus(false);
		} else if (CommonUtils.isNullOrEmpty(employee.getEmpId())) {
			finalResponse.setMessage("Emp Id   is null !");
			finalResponse.setStatus(false);
		} else if (CommonUtils.isNullOrEmpty(employee.getEmergencyNumber())) {
			finalResponse.setMessage("Emergency Number is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		}
		LOGGER.info("validation[END] input parameters:" + employee.toString());
		;
		return finalResponse;

	}

	/**
	 *
	 * @return list of Employee
	 */
	public ResponseEntity<Page<Employee>> fetchDataByList(int offset, int size, String field) {
		LOGGER.info("fetchDataByList[START-END] ");
		Page<Employee> findAll = empRepo.findAll(PageRequest.of(offset, size).withSort(Sort.by(Direction.ASC, field)));
		if (findAll.getSize() <= 0) {
			return new ResponseEntity<Page<Employee>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Page<Employee>>(findAll, HttpStatus.OK);
		}
	}

	/**
	 * @PathVariable id
	 * @return if Id is exists it returns the employee data
	 */
	public ResponseEntity<FinalResponse> fetchDataByEmpId(Long id) {
		LOGGER.info("fetchDataByEmpId[START]");
		Optional<Employee> employee = empRepo.findById(id);
		FinalResponse finalResponse = new FinalResponse();
		if (!employee.isPresent()) {
			finalResponse.setMessage("Employee is not found");
			finalResponse.setStatus(false);
			LOGGER.error("fetchDataByEmpId[] Employee is not found");
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
		} else {
			finalResponse.setMessage("Employee successfully getting");
			finalResponse.setStatus(true);
			finalResponse.setData(employee);
			LOGGER.info("fetchDataByEmpId[END]");
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
		}
	}

	/**
	 * @path action - delete , block or unblock
	 * @Path employee Id
	 * @return finalResponse with Employee
	 */
	public ResponseEntity<FinalResponse> manageUsers(String action, int id) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.info("manageUsers[START]");
		try {
			if (action.equalsIgnoreCase("delete")) {
				if (!empRepo.existsById((long) id)) {
					finalResponse.setMessage("Id is not exists!");
					finalResponse.setStatus(false);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
				} else {
					empRepo.deleteById((long) id);
					finalResponse.setMessage("Employee Delete Successfully...");
					finalResponse.setStatus(true);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
				}
			} else if (action.equalsIgnoreCase("block")) {
				if (!empRepo.existsById((long) id)) {
					finalResponse.setMessage("id is not exists!");
					finalResponse.setStatus(false);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
				} else {
					empRepo.blockUser(id);
					finalResponse.setMessage("Employee Block Successfully...");
					finalResponse.setStatus(true);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
				}
			} else if (action.equalsIgnoreCase("unblock")) {
				if (!empRepo.existsById((long) id)) {
					finalResponse.setMessage("id is not exists!");
					finalResponse.setStatus(false);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
				} else {
					empRepo.unBlockUser(id);
					finalResponse.setMessage("Employee unblock Successfully...");
					finalResponse.setStatus(true);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			LOGGER.error("manageUsers[Exception] Exception in manage user " + e.getMessage());
			return new ResponseEntity<FinalResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("manageUsers[END]");
		return new ResponseEntity<FinalResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public List<Employee> getAllEmployee() {
		LOGGER.info("getAllEmployee[START-END");
		List<Employee> findAll = empRepo.findAll();
		return findAll;
	}

	public ResponseEntity<FinalResponse> getByEmployeeName(String name) {
		FinalResponse finalResponse = new FinalResponse();
		Employee findByNameLike = empRepo.findByNameLike(name);
		if (findByNameLike == null) {
			finalResponse.setMessage("Name is not exists!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
		} else {
			finalResponse.setData(findByNameLike);
			finalResponse.setMessage("Success");
			finalResponse.setStatus(true);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
		}
	}
}
