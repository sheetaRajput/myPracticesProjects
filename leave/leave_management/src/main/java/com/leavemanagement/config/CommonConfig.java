package com.leavemanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class CommonConfig {

	@Value("${leaveCreateExce}")
	private String leaveCreateExce;
	
	@Value("${sucessMsg}")
	private String sucessMsg;
	
	@Value("${two}")
	private Long two;
	
	@Value("${employeeCreateExce}")
	private String employeeCreateExce;

	@Value("${empSuccess}")
	private String empSuccess;


	@Value("${deptCreateExce}")
	private String deptCreateExce;

	@Value("${empUpdated}")
	private String empUpdated;
	
	@Value("${headQuraterMail}")
	private String headQuraterMail;
	
	@Value("${leaveSuccess}")
	private String leaveSuccess;

	@Value("${endDay}")
	private int endDay;
	
	@Value("${leaveNotExists}")
	private String leaveNotExists;
}
