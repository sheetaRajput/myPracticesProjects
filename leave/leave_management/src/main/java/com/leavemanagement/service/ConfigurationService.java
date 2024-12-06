package com.leavemanagement.service;

import java.util.List;

import javax.validation.ValidationException;

import com.leavemanagement.controller.ConfigurationController;
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
import org.springframework.transaction.annotation.Transactional;

import com.leavemanagement.entity.Configuration;
import com.leavemanagement.repository.ConfigurationRepository;

@Service
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository repo;
	private  final Logger Logger = LoggerFactory.getLogger(ConfigurationController.class);

	public Configuration create(Configuration c) {
		Logger.info("create[START]");
		Configuration entity = read(c.getKey(), c.getType());
		if (null != entity) {
			throw new ValidationException("Configuration with the key pair already exists");
		}
		entity = c.to();
		Logger.info("create[END]");
		return repo.save(entity);
	}

	public Long update(Configuration c) {
        Logger.info("update[START");
		Configuration entity = read(c.getId());
		if (null == entity)
			throw new ValidationException("Configuration with the Identifier does not exist");

		entity.setValue(c.getValue());
		repo.save(entity);
		Logger.info("update[END]");
		return entity.getId();
	}
	

	public ResponseEntity<Page<Configuration>> list(int offset, int size, String field) {
		Logger.info("list[START]  ");
		Page<Configuration> findAll = repo.findAll(PageRequest.of(offset, size).withSort(Sort.by(Direction.ASC, field)));
		if (findAll.getSize() <= 0) {
			Logger.info("list[END] if list size is not available it throws a error");
			return new ResponseEntity<Page<Configuration>>(HttpStatus.NOT_FOUND);
		} else {
			Logger.info("list[END]");
			return new ResponseEntity<Page<Configuration>>(findAll, HttpStatus.OK);
		}
	}
	
	public Configuration read(String key, String type) {
		Logger.info("read[START- END]");
		return repo.findByKeyAndType(key, type);
	}
	
	@Transactional(readOnly = true)
	public Configuration read(Long id) {
		Logger.info("read[START]");
		if (null == id)
			return null;
		Logger.info("read[END]");
		return repo.findById(id).orElse(null);
	}
	
	public String getValue(String value) {
		String value2 = repo.getValue(value);
		return value2;
	}
}
