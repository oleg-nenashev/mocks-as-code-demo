package com.example.todos.hn;

import java.util.Date;

public record HackernewsItem(
    String id, String title, String url, String by, Date time, Integer descendants
) {
}
