package com.example.cafe.controller;

import com.example.cafe.constants.CafeConstants;
import com.example.cafe.model.dto.BillDto;
import com.example.cafe.service.BillService;
import com.example.cafe.util.CafeUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(@NotNull final BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    @Operation(summary = "Create new bill")
    ResponseEntity<String> create(@NotNull @Valid @RequestBody final BillDto billDto) {
        try {
            return billService.generateReport(billDto);
        } catch (Exception ex) {
            log.error("Failed call create", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get all bills")
    ResponseEntity<List<BillDto>> findAll() {
        try {
            return billService.findAll();
        } catch (Exception ex) {
            log.error("Failed call findAll", ex);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}/pdf")
    @Operation(summary = "Get pdf report for given bill id")
    ResponseEntity<byte[]> findPdf(@PathVariable final Integer id) {
        try {
            return billService.findPdf(id);
        } catch (Exception ex) {
            log.error("Failed call findPdf", ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete bill")
    ResponseEntity<String> delete(@PathVariable final Integer id) {
        try {
            return billService.delete(id);
        } catch (Exception ex) {
            log.error("Failed call delete", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}