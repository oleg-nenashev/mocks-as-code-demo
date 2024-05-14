package com.atomicjar.todos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.*;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "order_number")
    private Integer order;

    public Todo() {
    }

    public Todo(String id, String title, String link, Boolean completed, Integer order) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @JsonIgnore
    public String getUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/todos/" + this.getId();
    }

    @Override
    public String toString() {
        return "Todo{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", completed=" + completed +
            ", order=" + order +
            '}';
    }
}
