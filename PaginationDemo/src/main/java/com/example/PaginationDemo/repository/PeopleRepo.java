package com.example.PaginationDemo.repository;

import com.example.PaginationDemo.entity.PeopleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepo extends JpaRepository<PeopleInfo,Long> {
}
