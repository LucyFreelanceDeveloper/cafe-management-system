package com.example.cafe.repository;

import com.example.cafe.model.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillEntity, Integer> {
}