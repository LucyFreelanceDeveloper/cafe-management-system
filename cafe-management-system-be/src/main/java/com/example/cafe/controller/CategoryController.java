package com.example.cafe.controller;

import com.example.cafe.constants.CafeConstants;
import com.example.cafe.model.dto.CategoryDto;
import com.example.cafe.service.CategoryService;
import com.example.cafe.util.CafeUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(@NotNull final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Create category")
    ResponseEntity<String> create(@NotNull @Valid @RequestBody final CategoryDto categoryDto) {
        try {
            return categoryService.create(categoryDto);
        } catch (Exception ex) {
            log.error("Failed call create: %s", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    ResponseEntity<List<CategoryDto>> findAll() {
        try {
            return categoryService.findAll();
        } catch (Exception ex) {
            log.error("Failed call findAll: %s", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @Operation(summary = "Update category")
    ResponseEntity<String> update(@RequestBody final CategoryDto categoryDto) {
        try {
            return categoryService.update(categoryDto);
        } catch (Exception ex) {
            log.error("Failed call update: %s", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete category")
    ResponseEntity<String> delete(@PathVariable final Integer id) {
        try {
            return categoryService.delete(id);
        } catch (Exception ex) {
            log.error("Failed call delete: %s", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}