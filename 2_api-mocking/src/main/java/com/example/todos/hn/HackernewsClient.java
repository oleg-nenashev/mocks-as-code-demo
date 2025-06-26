package com.example.todos.hn;

import com.example.todos.ai.AiService;
import com.example.todos.ai.HackernewsItemResult;
import com.example.todos.entity.Todo;
import com.example.todos.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.function.Consumer;

// class to query the Hackernews API, and return HackernewsItem objects
@Component
public class HackernewsClient {

  @Value( "${hackernews.base-url:https://hacker-news.firebaseio.com/v0/}" )
  private String baseUrl;

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private AiService aiService;


  // method to return Spring WebClient object for querying the Hackernews API
  public WebClient getWebClient() {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  // method to query the Hackernews API, and return a HackernewsItem object
  public void getTopStories(int n) {
    Query.getTopStories(getWebClient(), n, hnItem ->
      {
        todoFromHNItem(hnItem);
      });
  }

  public static class Query {

    public static void getTopStories(WebClient client, int n, Consumer<HackernewsItem> consumer) {
        getStories("/topstories.json", client, n, consumer);
    }

    public static void getBestStories(WebClient client, int n, Consumer<HackernewsItem> consumer) {
        getStories("/beststories.json", client, n, consumer);
    }

    public static void getStories(String endpoint, WebClient client, int n, Consumer<HackernewsItem> consumer) {
        client.get()
              .uri(endpoint)
              .retrieve()
              .bodyToFlux(Integer.class)
              .take(n)
              .flatMap(id -> client.get()
                      .uri("/item/{id}.json", id)
                      .retrieve()
                      .bodyToMono(HackernewsItem.class))
              .subscribe(consumer);
    }
  }

  private void todoFromHNItem(HackernewsItem hnItem) {
    String title = hnItem.title();
    List<Todo> byTitle = todoRepository.findByTitle(title);
    if(byTitle.isEmpty()) {
      HackernewsItemResult assessment = aiService.assess(hnItem);

      Todo todo;
      if (assessment != null) {
        todo = new Todo(null, title, hnItem.url(), false, hnItem.descendants(),
                       assessment.summary(), assessment.priority(), assessment.timeEstimate(), assessment.sentiment());
        System.out.println("Created Todo with HackernewsItemResult: " + assessment);
      } else {
        todo = new Todo(null, title, hnItem.url(), false, hnItem.descendants());
        System.out.println("Created Todo without HackernewsItemResult");
      }

      todoRepository.save(todo);
    }
  }

}
