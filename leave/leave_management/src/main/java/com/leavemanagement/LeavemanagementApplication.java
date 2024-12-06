package com.leavemanagement;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.leavemanagement.config.Seed;

import ch.qos.logback.classic.Logger;
import lombok.SneakyThrows;

/**
 * @author Aamir Majeed Mir
 * @developer Sheetal Devi
 * @developer Akash kumar
 * @tester Prince Rana
 */

@SpringBootApplication
@EnableScheduling
public class LeavemanagementApplication {

	private final org.jboss.logging.Logger LOGGER = LoggerFactory.logger(LeavemanagementApplication.class);
	/**
	 * Swagger URL http://localhost:8080/leave-management/swagger-ui.html
	 * 
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		SpringApplication.run(LeavemanagementApplication.class, args);
		LeavemanagementApplication e=new LeavemanagementApplication();
//		e.test();
	}

	@Autowired
	private Seed seed;

	@SneakyThrows
	private void init() {
		System.out.println("Akash kumar is back");
		seed.configSetup();
	}

//	@Scheduled(cron="* * * 1 1 *")
//	public void test() {
//		LOGGER.info("Testing for that"+ LocalDateTime.now());
//		System.out.println("Heelo this is testing for that "+ new Date());
//	}
	
}
