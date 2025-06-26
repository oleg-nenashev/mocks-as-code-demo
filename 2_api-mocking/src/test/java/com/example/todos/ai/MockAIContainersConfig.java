package com.example.todos.ai;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.DockerModelRunnerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;


@TestConfiguration(proxyBeanMethods = false)
public class MockAIContainersConfig {

    @Bean
    WireMockContainer wireMockContainer() {
        var container = new WireMockContainer("wiremock/wiremock:3.13.0")
            .withMappingFromResource("hackernews", "mappings/hackernews_v0-stubs.json")
             .withMappingFromResource("AI Model", "ai/mappings/aimodel.json");    ;

        return container;
    }

    @Bean
    DynamicPropertyRegistrar apiPropertiesRegistrar(WireMockContainer wireMockContainer) {
        return registry -> {
            registry.add("hackernews.base-url", wireMockContainer::getBaseUrl);
            registry.add("spring.ai.openai.base-url", wireMockContainer::getBaseUrl);
        };
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer(){
        return new PostgreSQLContainer<>("postgres:15-alpine");
    }

}
