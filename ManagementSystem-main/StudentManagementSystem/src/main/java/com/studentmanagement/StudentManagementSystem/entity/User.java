package com.studentmanagement.StudentManagementSystem.entity;

;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "student")
public class User{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String degree;
    @Column(unique = true)
    private String contactNumber;
    private String fatherName;
    private String address;
    private String dob;
    private String gender;
    private String username;
    private String password;
    private String role;

    public User() {
    }

    public User(Long id, String name, String degree, String contactNumber, String fatherName, String address, String dob, String gender, String username, String password,String role) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.contactNumber = contactNumber;
        this.fatherName = fatherName;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.role= role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", degree='" + degree + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
