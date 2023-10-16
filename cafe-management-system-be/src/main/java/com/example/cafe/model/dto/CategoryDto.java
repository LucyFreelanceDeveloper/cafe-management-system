package com.example.cafe.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {
    private Integer id;

    @NotNull(message = "Name cannot be null")
    private String name;
}