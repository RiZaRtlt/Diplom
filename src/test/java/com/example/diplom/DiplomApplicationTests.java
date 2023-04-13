package com.example.diplom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiplomApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private GenericContainer<?> myApp = new GenericContainer<>("app:latest")
            .withExposedPorts(8080);

    @BeforeEach
    void setUp() {
        myApp.start();
    }

    @Test
    void contextLoads() {
        Integer port = myApp.getMappedPort(8080);
    }

}
