package com.leavemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.ApplyLeave;

@Repository
public interface LeaveRepo extends JpaRepository<ApplyLeave, Long> {
	List<ApplyLeave> findByEmpName(String empName);


	List<ApplyLeave> findApplyLeavesByEmpId(int empId);
}
