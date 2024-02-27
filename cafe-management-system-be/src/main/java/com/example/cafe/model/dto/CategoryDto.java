package com.example.cafe.model.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryDto(Integer id, @NotNull(message = "Name cannot be null") String name) {
}