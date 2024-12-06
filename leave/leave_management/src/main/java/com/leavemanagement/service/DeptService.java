package com.leavemanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.leavemanagement.config.CommonConfig;
import com.leavemanagement.controller.DeptController;
import com.leavemanagement.entity.Department;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.repository.DeptRepo;
import com.leavemanagement.utils.CommonUtils;

@Service
public class DeptService {

	@Autowired
	private DeptRepo deptRepo;
	private final Logger LOGGER = LoggerFactory.getLogger(DeptController.class);
    @Autowired
	private CommonConfig config;

	public ResponseEntity<Page<Department>> getAllDepartment(int offSet, int pageSize, String field) {
		LOGGER.info("getAllDepartment[START]");
		Page<Department> findAll = deptRepo.findAll(PageRequest.of(offSet, pageSize).withSort(Sort.by(field)));
		if (findAll.getSize() <= 0) {
			LOGGER.error("getAllDepartment[END]");
			return new ResponseEntity<Page<Department>>(HttpStatus.NOT_FOUND);
		} else {
			LOGGER.info("getAllDepartment[END]");
			return new ResponseEntity<Page<Department>>(findAll, HttpStatus.OK);
		}
	}

	/**
	 * @RequestBody Department
	 * @return finalResponse with department
	 */
	public ResponseEntity<FinalResponse> createDept(Department department) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.info("createDept[START]");
		try {
			if (CommonUtils.isNullOrEmpty(department.getDepartmentName())) {
				finalResponse.setStatus(false);
				finalResponse.setMessage("Department Name is null !");
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NO_CONTENT);
			}
			Department findByDepartmentName = deptRepo
					.findByDepartmentName(CommonUtils.convertString(department.getDepartmentName()));
			if (CommonUtils.isNullOrEmpty(department.getId())) {
				if (CommonUtils.isNullOrEmpty(findByDepartmentName)) {
					department.setDepartmentName(CommonUtils.convertString(department.getDepartmentName()));
					Department save = deptRepo.save(department);
					finalResponse.setData(save);
					finalResponse.setMessage("Department successfully create !");
					finalResponse.setStatus(true);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.CREATED);
				} else {
					finalResponse.setMessage(CommonUtils.convertString(department.getDepartmentName())
							+ " Department is already exits !");
					finalResponse.setStatus(false);
					LOGGER.info("createDept[END]");
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.CONFLICT);
				}
			} else {
				if (CommonUtils.isNullOrEmpty(findByDepartmentName)) {
					department.setDepartmentName(CommonUtils.convertString(department.getDepartmentName()));
					Department save = deptRepo.save(department);
					finalResponse.setData(save);
					finalResponse.setMessage("Department successfully updated !");
					finalResponse.setStatus(true);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
				} else {
					finalResponse.setMessage(CommonUtils.convertString(department.getDepartmentName())
							+ " Department is already exits !");
					finalResponse.setStatus(false);
					LOGGER.info("createDept[END]");
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.CONFLICT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(config.getDeptCreateExce() + e.getMessage());
			finalResponse.setMessage(e.getMessage());
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
