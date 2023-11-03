package com.example.cafe.service;

import com.example.cafe.model.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<String> create(CategoryDto categoryDto);

    ResponseEntity<List<CategoryDto>> findAll();

    ResponseEntity<String> update(CategoryDto categoryDto);

    ResponseEntity<String> delete(Integer id);
}