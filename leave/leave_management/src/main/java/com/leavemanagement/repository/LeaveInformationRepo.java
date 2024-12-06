package com.leavemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.leavemanagement.entity.LeaveInformation;

@Repository
@EnableJpaRepositories
public interface LeaveInformationRepo extends JpaRepository<LeaveInformation, Long>{

}
