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
@Table(name = "\"user\"")
public class UserEntity extends BasicIdEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;
}