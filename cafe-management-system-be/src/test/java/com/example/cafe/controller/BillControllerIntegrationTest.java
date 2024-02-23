package com.example.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BillControllerIntegrationTest {

    public static final String ADMIN_MAIL = "admin@mailnator.com";
    public static final String ADMIN_PASSWORD = "12345";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    void create() throws Exception {
        Map<String, Object> createProductDetail = new HashMap<>();
        createProductDetail.put("id", 23);
        createProductDetail.put("name", "Beef Stroganoff");
        createProductDetail.put("price", 12);
        createProductDetail.put("total", 12);
        createProductDetail.put("category", "Meals");
        createProductDetail.put("quantity", "1");

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("name", "jirka");
        createRequest.put("email", "user@mailnator.com");
        createRequest.put("contactNumber", "1234567890");
        createRequest.put("paymentMethod", "Credit Card");
        createRequest.put("total", "10");
        createRequest.put("productDetail", createProductDetail);  // TODO: transform 'createProductDetail' to JSON
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity("/bills", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void findAll() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/bills", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findPdf() {
    }

    @Test
    void delete() {
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