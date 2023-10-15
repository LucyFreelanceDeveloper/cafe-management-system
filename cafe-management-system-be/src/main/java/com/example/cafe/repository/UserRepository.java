package com.example.cafe.repository;

import com.example.cafe.model.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.status = :status where u.id = :id")
    Integer updateStatus(@Param(value = "status") String status, @Param(value = "id") Integer id);

    List<UserEntity> findByRole(String role);
}