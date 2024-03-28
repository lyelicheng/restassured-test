package com.llye.test.restassuredtest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class APITest {

    @LocalServerPort
    protected int port;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost/api";
    }

    @Order(1)
    @Test
    @DisplayName("Create Customer Test")
    void testCreateCustomer() {
        RequestSpecification requestSpecification = buildRequestSpecification();

        String requestBody = """
                {
                    "firstName": "John",
                    "lastName": "Smith"
                }
                """;

        given(requestSpecification)
                .body(requestBody)
                .when()
                .post("/v1/customers")
                .then()
                .statusCode(201);
    }

    @Order(2)
    @Test
    @DisplayName("Get Customer Test")
    void testGetAllCustomers() {
        RequestSpecification requestSpecification = buildRequestSpecification();

        given(requestSpecification)
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 5)
                .when()
                .get("/v1/customers")
                .then()
                .statusCode(200)
                .body("id", contains(1))
                .body("firstName", contains("John"))
                .body("lastName", contains("Smith"));
    }

    private RequestSpecification buildRequestSpecification() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        return new RequestSpecBuilder()
                .setPort(port)
                .setContentType("application/json")
                .build();
    }
}
