package com.llye.test.restassuredtest;

import com.llye.test.restassuredtest.dto.CustomerDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
    @ParameterizedTest
    @MethodSource("buildRequestBody")
    @DisplayName("Create Customer Test")
    void testCreateCustomer(String firstName, String lastName) {
        RequestSpecification requestSpecification = buildRequestSpecification();

        String requestBody = String.format(
        """
        {
            "firstName": "%s",
            "lastName": "%s"
        }
        """, firstName, lastName);

        given(requestSpecification)
                .body(requestBody)
                .when()
                .post("/v1/customers")
                .then()
                .statusCode(201);
    }

    private static Stream<Arguments> buildRequestBody() {
        return Stream.of(
                arguments("John", "Smith"),
                arguments("James", "Smith"),
                arguments("Jason", "Smith")
        );
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
        assertEquals(3, customerDtoResp.length);
        assertEquals(3, customerDtoResp[0].getId());
        assertEquals("Jason", customerDtoResp[0].getFirstName());
        assertEquals("Smith", customerDtoResp[0].getLastName());
        assertEquals(2, customerDtoResp[1].getId());
        assertEquals("James", customerDtoResp[1].getFirstName());
        assertEquals("Smith", customerDtoResp[1].getLastName());
        assertEquals(1, customerDtoResp[2].getId());
        assertEquals("John", customerDtoResp[2].getFirstName());
        assertEquals("Smith", customerDtoResp[2].getLastName());
    }

    private RequestSpecification buildRequestSpecification() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        return new RequestSpecBuilder()
                .setPort(port)
                .setContentType("application/json")
                .build();
    }
}
