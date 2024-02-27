package com.example.cafe.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "bill")
public class BillEntity extends BasicIdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "total")
    private Integer total;

    @Column(name = "product_detail", columnDefinition = "json")
    private String productDetail;

    @Column(name = "created_by")
    private String createdBy;

}