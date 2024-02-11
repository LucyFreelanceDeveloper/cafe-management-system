package com.example.cafe.repository;

import com.example.cafe.model.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryIntegrationTest {

    private final Integer userId = 1;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void findUser() {
        final Optional<UserEntity> adminWrapper = userRepository.findById(userId);
        assertTrue(adminWrapper.isPresent());
        final Optional<UserEntity> userWrapper = userRepository.findById(2);
        assertTrue(userWrapper.isPresent());

        final UserEntity admin = adminWrapper.get();
        assertEquals("Admin", admin.getName());
        assertEquals(1, admin.getId());
        final UserEntity user = userWrapper.get();
        assertEquals("User", user.getName());
        assertEquals(2, user.getId());
    }

    @Test
    public void findNotExistenUser() {
        final Optional<UserEntity> userWrapper = userRepository.findById(65);
        assertTrue(userWrapper.isEmpty());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void createUser() {
        UserEntity user = new UserEntity();
        user.setName("User test");
        assertEquals("User test", user.getName());
    }

    @Test
    @DirtiesContext
    public void updateUser() {
        UserEntity user = userRepository.findById(userId).get();

        user.setName("Test name");

        userRepository.save(user);

        UserEntity updatedUser = userRepository.findById(userId).get();
        assertEquals("Test name", updatedUser.getName());
    }

    @Test
    @DirtiesContext
    public void deleteUser() {
        userRepository.deleteById(userId);
        assertTrue(userRepository.findById(userId).isEmpty());
    }
}
