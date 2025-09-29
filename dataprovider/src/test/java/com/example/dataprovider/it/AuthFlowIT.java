package com.example.dataprovider.it;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthFlowIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void register_login_and_access_protected_endpoint() {
        // 1) Register
        Map<String, Object> regBody = new HashMap<>();
        regBody.put("username", "it_user");
        regBody.put("password", "it_pass123");
        regBody.put("email", "it_user@example.com");

        ResponseEntity<Map> regResp = rest.postForEntity(baseUrl + "/api/auth/register", regBody, Map.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2) Login
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("username", "it_user");
        loginBody.put("password", "it_pass123");

        ResponseEntity<Map> loginResp = rest.postForEntity(baseUrl + "/api/auth/login", loginBody, Map.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).isNotNull();

        // token field name could be "token" or "accessToken" depending on your controller response
        Object tokenObj = loginResp.getBody().getOrDefault("token", loginResp.getBody().get("accessToken"));
        assertThat(tokenObj).isNotNull();
        String token = tokenObj.toString();
        assertThat(token).isNotBlank();

        // 3) Call a protected endpoint (subscriptions list)
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> subsResp = rest.exchange(
                baseUrl + "/api/subscriptions", HttpMethod.GET, entity, String.class);

        // Expect 200 OK and JSON array (possibly empty)
        assertThat(subsResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(subsResp.getBody()).isNotNull();
        assertThat(subsResp.getBody()).startsWith("[");
    }
}
