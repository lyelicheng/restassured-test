package com.llye.test.restassuredtest;

import com.llye.test.restassuredtest.dto.CustomerDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Response response = given(requestSpecification)
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 5)
                .when()
                .get("/v1/customers")
                .then()
                .statusCode(200)
                .extract()
                .response();

        ResponseBody<?> responseBody = response.getBody();
        CustomerDto[] customerDtoResp = responseBody.as(CustomerDto[].class);
        assertEquals(1, customerDtoResp.length);
        assertEquals(1, customerDtoResp[0].getId());
        assertEquals("John", customerDtoResp[0].getFirstName());
        assertEquals("Smith", customerDtoResp[0].getLastName());
    }

    private RequestSpecification buildRequestSpecification() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        return new RequestSpecBuilder()
                .setPort(port)
                .setContentType("application/json")
                .build();
    }
}
