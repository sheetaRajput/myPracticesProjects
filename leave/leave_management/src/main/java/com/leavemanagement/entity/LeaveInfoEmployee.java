package com.leavemanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="employee_leave_info")
@Data
public class LeaveInfoEmployee {


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int leaveId;
	
	private int employeeId;
	
	private int earnedLeave;
	
	private int covidLeave;
	
	private int inicidentalLeave;	
	
	private int leaveWithoutPay;
	  
	private int shortLeave;
	
	private int wfh;
}
