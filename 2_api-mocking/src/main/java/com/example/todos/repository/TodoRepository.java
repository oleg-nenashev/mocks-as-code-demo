package com.example.todos.repository;

import com.example.todos.entity.Todo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface TodoRepository extends ListCrudRepository<Todo, String>, ListPagingAndSortingRepository<Todo, String> {
    @Query("select t from Todo t where t.completed = false")
    List<Todo> getPendingTodos();

    List<Todo> findByTitle(String title);
}
