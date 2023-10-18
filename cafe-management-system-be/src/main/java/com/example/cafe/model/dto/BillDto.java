package com.example.cafe.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillDto {
    private Integer id;

    private String uuid;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "ContactNumber cannot be null")
    private String contactNumber;

    @NotNull(message = "PaymentMethod cannot be null")
    private String paymentMethod;

    @NotNull(message = "Total cannot be null")
    private Integer total;

    @NotNull(message = "ProductDetail cannot be null")
    private String productDetail;

    private String createdBy;
}