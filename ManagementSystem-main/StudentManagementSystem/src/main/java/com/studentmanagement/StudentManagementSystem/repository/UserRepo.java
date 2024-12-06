package com.studentmanagement.StudentManagementSystem.repository;


import com.studentmanagement.StudentManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

 import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

 public boolean existsByUsername(String username);
 public User findByUsername(String username);

//    Student findByRollNo(String rollNo);

}
