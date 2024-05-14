package com.atomicjar.todos;

import com.atomicjar.todos.entity.Todo;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = {ContainersConfig.class})
class ApplicationTests {

    @Test
    void contextLoads() {
        given(requestSpecification)
            .when().post("/todos/hn")
            .then().statusCode(200);


        await().atMost(10, SECONDS).untilAsserted(() -> {
            JsonPath jsonPath = given(requestSpecification)
                .when().get("/todos")
                .then().statusCode(200)
                .extract().body().jsonPath();

            List<Todo> todos = jsonPath.getList("", Todo.class);
            System.out.println(todos);
            Assertions.assertThat(todos).hasSize(6);
        });
    }

    protected RequestSpecification requestSpecification;

    @LocalServerPort
    protected int localServerPort;

    @BeforeEach
    public void setUpAbstractIntegrationTest() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecification = new RequestSpecBuilder()
            .setPort(localServerPort)
            .addHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
            )
            .build();
    }

}
