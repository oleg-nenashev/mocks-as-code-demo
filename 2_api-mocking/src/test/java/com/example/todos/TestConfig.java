package com.example.todos;

import com.example.todos.ai.AiService;
import com.example.todos.ai.HackernewsItemResult;
import com.example.todos.hn.HackernewsItem;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public AiService aiService() {
        AiService mockAiService = Mockito.mock(AiService.class);
        Mockito.when(mockAiService.assess(Mockito.any(HackernewsItem.class)))
                .thenReturn(new HackernewsItemResult("Test summary", "medium", "5 min", "neutral"));
        return mockAiService;
    }
}