package com.example.cafe.controller;

import com.example.cafe.CaffeManagementSystemApplication;
import com.example.cafe.model.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {CaffeManagementSystemApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

    public static final String ADMIN_MAIL = "admin@mailnator.com";
    public static final String ADMIN_PASSWORD = "12345";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    void createProduct() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("categoryId", "1");
        createRequest.put("name", "Pizza test");
        createRequest.put("description", "Test");
        createRequest.put("status", "Available");
        createRequest.put("price", "10");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity("/products", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void findAll() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/products", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findById() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/products/1", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findByCategoryId() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        // Vytvoření query parametru pro categoryId
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("categoryId", "1"); // Nastavim categoryId na požadovanou hodnotu

        // Vytvoření URL s query parametry
        URI uri = UriComponentsBuilder.fromUriString("/products")
                .queryParams(queryParams)
                .build()
                .toUri();

        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<ProductDto>>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ProductDto> products = response.getBody();
        assertThat(products).isNotNull();

        if (!products.isEmpty()) {
            ProductDto firstProduct = products.stream().filter(p->p.id().equals(2)).findFirst().orElse(null);
            Integer firstProductCategoryId = firstProduct.categoryId();
            Integer firstProductId = firstProduct.id();
            Integer firstProductPrice = firstProduct.price();
            String firstProductCategoryName = firstProduct.categoryName();
            String firstProductName = firstProduct.name();

            assertThat(products.size()).isEqualTo(10);
            assertThat(firstProductCategoryId).isEqualTo(1);
            assertThat(firstProductId).isEqualTo(2);
            assertThat(firstProductPrice).isEqualTo(4);
            assertThat(firstProductCategoryName).isEqualTo("Cafe");
            assertThat(firstProductName).isEqualTo("Cappuccino");
        }
    }

    @Test
    @DirtiesContext
    void update() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("categoryId", "1");
        createRequest.put("name", "Pizza 1");
        createRequest.put("description", "Test");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(createRequest, loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity("/products", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DirtiesContext
    void delete() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>("application/json", loginAdminUserHeaders());

        ResponseEntity<String> response = restTemplate.exchange("/products/1", HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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