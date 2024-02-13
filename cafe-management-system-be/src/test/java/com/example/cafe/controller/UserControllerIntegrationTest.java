package com.example.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    public void createUser() throws Exception {
        Map<String, String> newUser = Map.of("contactNumber", "1234567819",
                "email", "jim@yahoo.com",
                "name", "Jim",
                "password", "12345");

        ResponseEntity<String> response = restTemplate.postForEntity("/user/signup",
                newUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginUser() {
        Map<String, String> loginRequest = Map.of("email", "admin@mailnator.com",
                "password", "12345");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginWithBadPassword() {
        Map<String, String> loginRequest = Map.of("email", "admin@mailnator.com",
                "password", "123");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllUsers() {
        Map<String, String> loginRequest = Map.of("email", "admin@mailnator.com",
                "password", "12345");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response = restTemplate.getForEntity("/get", String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void updateUser() {
        Map<String, String> loginRequest = Map.of("email", "admin@mailnator.com",
                "password", "12345");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, String> updateRequest = Map.of("status", "true");
        ResponseEntity<String> response = restTemplate.postForEntity("/update", updateRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void checkToken() {
        Map<String, String> loginRequest = Map.of("email", "admin@mailnator.com",
                "password", "12345");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response = restTemplate.getForEntity("/checkToken", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void changePassword() {
        Map<String, String> loginRequest = Map.of("email", "admin@mailnator.com",
                "password", "12345");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, String> changePasswordRequest = Map.of("oldPassword", "12345",
                "newPassword", "123");
        ResponseEntity<String> changePasswordResponse = restTemplate.postForEntity("/changePassword", changePasswordRequest, String.class);
        assertThat(changePasswordResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(changePasswordResponse.getBody()).contains("123");
    }
//    // Metoda pro přihlášení uživatele
//    private ResponseEntity<String> loginUser(String email, String password) throws Exception {
//        Map<String, String> loginRequest = new HashMap<>();
//        loginRequest.put("email", email);
//        loginRequest.put("password", password);
//        return restTemplate.postForEntity("/user/login", loginRequest, String.class);
//    }
//
//    // Metoda pro registraci nového uživatele
//    private ResponseEntity<String> signUpUser(String email, String password) throws Exception {
//        Map<String, String> signUpRequest = new HashMap<>();
//        signUpRequest.put("email", email);
//        signUpRequest.put("password", password);
//        // Další údaje pro registraci nového uživatele můžete přidat podle potřeby
//        return restTemplate.postForEntity("/users", signUpRequest, String.class);
//    }

    private String obtainAccessToken(String username, String password) throws Exception {
        Map<String, String> params = Map.of("email", username, "password", password);
        ResponseEntity response = restTemplate.postForEntity("/user/login", params, String.class);
        assertThat(response.getStatusCode().toString()).isEqualTo(HttpStatus.OK.toString());
        return objectMapper.readValue(response.getBody().toString(), Map.class).get("token").toString();
    }
}
