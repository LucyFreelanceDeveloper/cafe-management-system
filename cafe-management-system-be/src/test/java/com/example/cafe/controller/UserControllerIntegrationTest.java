package com.example.cafe.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    public void createUser() throws Exception {
        String accessToken = obtainAccessToken("admin@mailnator.com", "12345");

        String userString = "{\"contact_number\":\"1234567819\", \"email\":\"jim@yahoo.com\",\"name\":\"Jim\", \"password\":\"12345\", \"'role'\":\"Admin\", \"status\":\"true\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/users", userString,String.class);

//        String employeeString = "{\"contact_number\":\"1234567819\", \"email\":\"jim@yahoo.com\",\"name\":\"Jim\", \"password\":\"12345\", \"'role'\":\"Admin\", \"status\":\"true\"}";
//
//        mockMvc.perform(post("/users")
//                        .with(httpBasic("Admin","12345"))
////                .header("Authorization", "Bearer " + accessToken)
//                        .content(employeeString)
//                        .contentType(MediaType.APPLICATION_JSON))
//      .andExpect(status().isCreated());
//
//        mockMvc.perform(get("/users")
//                        .param("email", "jim@yahoo.com")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .accept("application/json;charset=UTF-8"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        Map<String, String> params = Map.of("email",username, "password", password);
        ResponseEntity response = restTemplate.postForEntity("/user/login", params, String.class);
        assertThat(response.getStatusCode().toString()).isEqualTo(HttpStatus.OK.toString());
        return objectMapper.readValue(response.getBody().toString(), Map.class).get("token").toString();
    }
}
