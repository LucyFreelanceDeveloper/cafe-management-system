package com.example.cafe.repository;

import com.example.cafe.model.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Integer> {

    List<BillEntity> findByCreatedBy(String createdBy);
}