package com.leavemanagement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Emp_Name")
    @Size(min=3,max=50,message="min 2 and max 20 characters are allowed !!")
    private String name;

    @NotNull(message="Please enter your Mobile number!")
    @Size(message="digits should be 10")
    @Column(length = 10, unique = true, nullable = false, name = "Mobile_Number")
    private String mobileNumber;

    @Column(unique = true, nullable = false, name = "Email")
    @Email
    private String email;

    @Column(name = "Address",nullable = false)
    private String address;

    @Column(name = "Date_Of_Birth", nullable = false)
    private String dateOfBirth;

    @Column(name = "Emp_Id", nullable = false, unique = true)
    private int empId;

    @Column(name = "Emergency_Number", nullable = false)
    private String emergencyNumber;
    
    @Column(name = "active", nullable = false)
    private Boolean active;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(name = "leave_cl", joinColumns = {@JoinColumn(name = "emp_id", referencedColumnName = "id") }, inverseJoinColumns = {
//					@JoinColumn(name = "leave_id", referencedColumnName = "id") })
//	private Leave leave; 
}
