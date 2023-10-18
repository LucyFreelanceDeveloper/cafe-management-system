package com.example.cafe.service;

import com.example.cafe.model.dto.BillDto;
import org.springframework.http.ResponseEntity;


public interface BillService {
    ResponseEntity<String> generateReport(BillDto billDto);
}