package com.example.cafe.model.dto;

import com.example.cafe.model.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Integer id;

    private String name;

    private CategoryEntity category;

    private String description;

    private Integer price;

    private String status;
}