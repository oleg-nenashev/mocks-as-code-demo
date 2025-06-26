package com.example.todos.entity;

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

    @Column(name = "summary")
    private String summary;

    @Column(name = "priority")
    private String priority;

    @Column(name = "time_estimate")
    private String timeEstimate;

    @Column(name = "sentiment")
    private String sentiment;

    public Todo() {
    }

    public Todo(String id, String title, String link, Boolean completed, Integer order) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.link = link;
    }

    public Todo(String id, String title, String link, Boolean completed, Integer order, 
                String summary, String priority, String timeEstimate, String sentiment) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.link = link;
        this.summary = summary;
        this.priority = priority;
        this.timeEstimate = timeEstimate;
        this.sentiment = sentiment;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(String timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "Todo{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", completed=" + completed +
            ", order=" + order +
            ", summary='" + summary + '\'' +
            ", priority='" + priority + '\'' +
            ", timeEstimate='" + timeEstimate + '\'' +
            ", sentiment='" + sentiment + '\'' +
            '}';
    }
}
