package com.example.cafe.service;

import com.example.cafe.model.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<String> create(ProductDto productDto);
    ResponseEntity<List<ProductDto>> findAll();
    ResponseEntity<ProductDto> findById(Integer id);
    ResponseEntity<List<ProductDto>> findByCategoryId(Integer categoryId);
    ResponseEntity<String> update(ProductDto productDto);
    ResponseEntity<String> delete(Integer id);
}