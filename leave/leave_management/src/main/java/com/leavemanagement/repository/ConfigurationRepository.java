package com.leavemanagement.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.Configuration;

@Repository
@EnableJpaRepositories
public interface ConfigurationRepository extends JpaRepository<Configuration, Long>{
	Configuration findByKeyAndType(String key,String type);
	
	@Query(value="select value from configuration where value=?", nativeQuery = true)
	String getValue(String value);
}
