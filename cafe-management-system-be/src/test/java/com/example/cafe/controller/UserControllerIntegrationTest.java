package com.example.cafe.controller;

import com.example.cafe.CaffeManagementSystemApplication;
import com.example.cafe.model.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CaffeManagementSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:/application.properties")
public class UserControllerIntegrationTest {
    public static final String ADMIN_MAIL = "admin@mailnator.com";
    public static final String ADMIN_PASSWORD = "12345";
    public static final String ADMIN_ID = "1";
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
        Map<String, String> loginRequest = Map.of("email", ADMIN_MAIL,
                "password", ADMIN_PASSWORD);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginWithBadPassword() {
        Map<String, String> loginRequest = Map.of("email", ADMIN_MAIL,
                "password", "123");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllUsers() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>("application/json", loginAdminUserHeaders());

        UserEntity[] users = restTemplate.exchange("/user/get", HttpMethod.GET, entity, UserEntity[].class).getBody();
        assertEquals(2, users.length, "Expected 2 accounts, but found " + users.length);
        assertThat(users[0].getName()).isEqualTo("Admin");
        assertThat(users[0].getId()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void updateUser() throws Exception {
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("id", ADMIN_ID);
        updateRequest.put("status", "true");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updateRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/user/update", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void checkToken() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>("application/json", loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/user/checkToken", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void changePassword() throws Exception {
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("oldPassword", ADMIN_PASSWORD);
        updateRequest.put("newPassword", "123");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updateRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/user/changePassword", HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Password update successfully");
    }

    private HttpHeaders loginAdminUserHeaders() throws Exception {
        Map<String, String> loginRequest = Map.of("email", ADMIN_MAIL,
                "password", ADMIN_PASSWORD);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/user/login", loginRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + obtainAccessToken(ADMIN_MAIL, ADMIN_PASSWORD));
        return headers;
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        Map<String, String> params = Map.of("email", username, "password", password);
        ResponseEntity response = restTemplate.postForEntity("/user/login", params, String.class);
        assertThat(response.getStatusCode().toString()).isEqualTo(HttpStatus.OK.toString());
        return objectMapper.readValue(response.getBody().toString(), Map.class).get("token").toString();
    }
}
