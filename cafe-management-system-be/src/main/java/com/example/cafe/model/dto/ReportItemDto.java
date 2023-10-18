package com.example.cafe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemDto{

    private Integer id;

    private String name;

    private String category;

    private Integer quantity;

    private Double price;

    private Double total;

}