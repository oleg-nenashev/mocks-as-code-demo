package com.example.todos;

import com.example.todos.hn.HackernewsClient;
import com.example.todos.hn.HackernewsItem;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;


/**
 * It is a test configuration for Testcontainers isolating the target environment.
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Application.class, TestConfig.class})
@EnableWireMock(
        @ConfigureWireMock( name = "hackernews-server",
                resetWireMockServer = true,
                filesUnderDirectory = "src/test/resources")
)
class HackernewsClientTest {

    @Value("${wiremock.server.baseUrl}")
    private String wireMockUrl;

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(wireMockUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

//    @Test
//    public void hasTopStories() {
 //       List<HackernewsItem> items = new ArrayList<>();
  //      HackernewsClient.Query.getTopStories(getWebClient(), 5, items::add);
//
 //        Assert.assertEquals(5, items.size());
 //   }

    @Test
    public void loadsTopStoriesFromTheAPI() {
        given()
                .baseUri(wireMockUrl)
                .contentType(ContentType.JSON)
        .when()
                .get("/topstories.json")
        .then()
                .statusCode(200)
                .body(".", hasSize(4));
    }

}
