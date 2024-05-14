package com.atomicjar.todos;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.repository.TodoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.testcontainers.DockerClientFactory;

import java.sql.PreparedStatement;

// run this application with:
// ./mvnw spring-boot:test-run
//
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}

@Component
class DataLoader {

    private TodoRepository todoRepository;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public DataLoader(TodoRepository todoRepository, JdbcTemplate jdbcTemplate) {
        this.todoRepository = todoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void addTodo() {
        if(!todoRepository.findAll().isEmpty()) {
            return;
        }
        Todo t1 = new Todo();
        t1.setTitle("Learn about Testcontainers");
        todoRepository.save(t1);

        Todo t2 = new Todo();
        t2.setTitle("Learn about WireMock");
        todoRepository.save(t2);
    }
}
