package com.leavemanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "apply_leave")
public class ApplyLeave {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;

	@Column(name ="leave_type")
	private String leaveType;

	@Column(name ="reason")
	@NotNull(message = "please provide a reason for a leave! ")
	private String reason;

	@Column(name ="leave_status")
	private String leaveStatus;
	
	@Column(name ="to_date")
	@NotNull(message = "please provide a to date for a leave! ")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String toDate;
	
	@Column(name ="from_date")
	@NotNull(message = "please provide a from date for a leave! ")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String fromDate;

	@Column(name ="no_of_hours")
	private String noOfHours;
	
	@Column(name ="no_of_days")
	private int noOfDays;
	
	@NotNull(message = "please provide a AlterNative Resource for a leave! ")
	@Column(name="alter_native_resource")
	private String alterNativeResource;
	
	@NotNull(message = "please provide a Department for a leave! ")
	@Column(name="department")
	private String department;
	
	@NotNull(message = "please provide a Employee Name for a leave! ")
	@Column(name="emp_name")
	private String empName;

	@Column(name = "empId", nullable = false)
	private int empId;
	
	@NotNull(message = "please provide a Employee Name for a leave! ")
	@Column(name="team_leader")
	private String teamLeader;


//	@NotNull(message = "Please give a employee id!")
//	@ManyToOne(cascade = CascadeType.ALL)
//	private Employee empId;

}
