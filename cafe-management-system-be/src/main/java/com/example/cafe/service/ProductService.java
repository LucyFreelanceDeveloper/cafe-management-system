package com.example.cafe.service;

import com.example.cafe.model.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<String> create(ProductDto productDto);

    ResponseEntity<List<ProductDto>> findAll();

    ResponseEntity<String> update(ProductDto productDto);
}