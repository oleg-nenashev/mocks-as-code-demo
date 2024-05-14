package com.atomicjar.todos.hn;

import com.atomicjar.todos.entity.Todo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public record HackernewsItem(
    String id, String title, String url, String by, Date time, Integer descendants
) {
}
