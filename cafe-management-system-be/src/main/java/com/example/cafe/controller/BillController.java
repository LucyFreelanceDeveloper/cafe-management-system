package com.example.cafe.controller;

import com.example.cafe.constants.CafeConstants;
import com.example.cafe.model.dto.BillDto;
import com.example.cafe.service.BillService;
import com.example.cafe.util.CafeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    ResponseEntity<String> create(@NotNull @Valid @RequestBody BillDto billDto) {
        try {
            return billService.generateReport(billDto);
        } catch (Exception ex) {
            log.error("Failed call create", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    ResponseEntity<List<BillDto>> findAll() {
        return null;
    }
}