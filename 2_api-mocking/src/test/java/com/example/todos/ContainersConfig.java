package com.example.todos;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.DockerModelRunnerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;


@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    @Bean
    DockerModelRunnerContainer dockerModelRunnerContainer() {
        var container = new DockerModelRunnerContainer("alpine/socat:1.8.0.1");
        return container;
    }

    @Bean
    WireMockContainer wireMockContainer() {
        var container = new WireMockContainer("wiremock/wiremock:3.13.0")
            .withMappingFromResource("hackernews", "mappings/hackernews_v0-stubs.json");

        return container;
    }

    @Bean
    DynamicPropertyRegistrar apiPropertiesRegistrar(WireMockContainer wireMockContainer, DockerModelRunnerContainer dockerModelRunnerContainer) {
        return registry -> {
            registry.add("hackernews.base-url", wireMockContainer::getBaseUrl);
            registry.add("spring.ai.openai.base-url", dockerModelRunnerContainer::getOpenAIEndpoint);
        };
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer(){
        return new PostgreSQLContainer<>("postgres:15-alpine");
    }

}
