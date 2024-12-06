package com.leavemanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity
public class LeaveInformation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private int earnedLeave;
	
	private int covidLeave;
	
	private int inicidentalLeave;	
	
	private int leaveWithoutPay;
	  
	private int shortLeave;
	
	private int wfh;
}
