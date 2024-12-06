package com.leavemanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.leavemanagement.dectionary.TYPES;
import com.leavemanagement.entity.Configuration;
import com.leavemanagement.repository.ConfigurationRepository;

@Transactional
@Component
public class Seed {

	private final Logger LOGGER = LoggerFactory.getLogger(Seed.class);

	@Autowired
	private ConfigurationRepository repo;

	public void configSetup() {
		boolean seed = true;
		if (!seed) {
			return;
		}

		LOGGER.info("************************ App Seeding Started ************************");
		saveConfig(TYPES.TYPES, "Configuration Types");
		LOGGER.info("************************ App Seeded ************************");
	}

	private void saveConfig(ConfigurationEnum cnf, String value) {
		saveConfig(cnf.getType(), cnf.getKey(), value);
	}

	private void saveConfig(String type, String key, String value) {
		if (null == (repo.findByKeyAndType(key, type))) {
			LOGGER.info("Seed : {}, {}", key, type);
			repo.save(Configuration.builder().key(key).value(value).type(type).build());
		}
	}
}
