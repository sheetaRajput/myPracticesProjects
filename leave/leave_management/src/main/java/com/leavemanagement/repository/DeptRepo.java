package com.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.Department;

@Repository
public interface DeptRepo extends JpaRepository<Department, Long>{
   Department findByDepartmentName(String departmentName);
}
