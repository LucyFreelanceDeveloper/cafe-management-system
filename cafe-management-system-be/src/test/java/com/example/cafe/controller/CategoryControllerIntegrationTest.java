package com.example.cafe.controller;

import com.example.cafe.CaffeManagementSystemApplication;
import com.example.cafe.model.dto.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = {CaffeManagementSystemApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerIntegrationTest {

    public static final String ADMIN_MAIL = "admin@mailnator.com";
    public static final String ADMIN_PASSWORD = "12345";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    void shouldCreateCategory() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("name", "Pizza americano");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity("/categories", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isEqualTo("{\"message\":\"Category Added Successfully: [id:11]\"}");
    }

    @Test
    @DirtiesContext
    void shouldReturn400WhenCreateCategory() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("id", 1);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity("/categories", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertTrue(responseBody.contains("Name cannot be null"));
    }

    @Test
    @DirtiesContext
    void shouldReturn400AndCheckIfNameIsStringWhenCreateCategory() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("name", 1);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity("/categories", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertTrue(responseBody.contains("Category name must not be empty and must not contain numbers"));
    }

    @Test
    void shouldFindAllCategories() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange("/categories", HttpMethod.GET, entity, new ParameterizedTypeReference<List<CategoryDto>>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<CategoryDto> responseBody = response.getBody();
        assertThat(responseBody).isNotNull();

        if (!responseBody.isEmpty()) {
            CategoryDto firstCategory = responseBody.get(0);
            Integer firstCategoryId = firstCategory.id();
            String firstCategoryName = firstCategory.name();

            assertThat(responseBody.size()).isEqualTo(10);
            assertThat(firstCategoryId).isEqualTo(1);
            assertThat(firstCategoryName).isEqualTo("Cafe");
        }
    }

    @Test
    @DirtiesContext
    void shouldUpdateCategory() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("id", "1");
        createRequest.put("name", "Pizza1");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/categories", HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertTrue(responseBody.contains("Category Updated Successfully"));
    }

    @Test
    @DirtiesContext
    void shouldReturn400WhenUpdateCategory() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("id", "25");
        createRequest.put("name", "Pizza1");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/categories", HttpMethod.PUT, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertTrue(responseBody.contains("Category id does not exist"));
    }

    @Test
    @DirtiesContext
    void shouldDeleteCategory() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/categories/1", HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertTrue(responseBody.contains("Category Delete Successfully"));
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