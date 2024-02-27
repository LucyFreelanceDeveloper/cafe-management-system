package com.example.cafe.model.dto;

public record ProductDto(
        Integer id,
        String name,
        String description,
        Integer price,
        String status,
        Integer categoryId,
        String categoryName
) {
}