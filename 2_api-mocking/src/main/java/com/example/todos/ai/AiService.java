package com.example.todos.ai;

import com.example.todos.hn.HackernewsItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for AI-related functionality.
 * This class provides methods for interacting with the OpenAI API.
 */
@Service
public class AiService {

    private final ChatClient newsAssistant;
    private final ObjectMapper objectMapper;

    public AiService(ChatClient newsAssistant) {
        this.newsAssistant = newsAssistant;
        this.objectMapper = new ObjectMapper();
    }

    public HackernewsItemResult assess(HackernewsItem item) {
            // If parsing fails, it might be due to <think> tags in the response
            String rawContent = newsAssistant.prompt()
                    .advisors(new SimpleLoggerAdvisor())
                    .user(u ->u.text("Hacker News Item: is {item}").param("item", item))
                    .call()
                    .content();


            // Remove any <think>text</think> tags from the raw content
            String cleanedContent = rawContent.replaceAll("<think>.*?</think>", "");

            // Remove backticks and "json" markers
            cleanedContent = cleanedContent.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            System.out.println("cleanedContent: " + cleanedContent);
            // Try to parse the cleaned content
            try {
                // Use the class-level ObjectMapper to convert the cleaned content to HackernewsItemResult
                return objectMapper.readValue(cleanedContent, HackernewsItemResult.class);
            } catch (Exception ex) {
                // If all else fails, log the error and return null
                System.err.println("Failed to parse response: " + ex.getMessage());
                return null;
            }
    }

}

@Configuration
class ConversationalConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {

        var system = """
                You are an AI assistant that processes Hacker News articles to help users quickly understand and prioritize them.

                 Given the following article metadata:
                 ID: {id} 
                 Title: {title}
                 URL: {url} 
                 Author: {by} 
                 Timestamp: {time}
                 Number of Comments: {descendants}
                 Perform the following:

                 Summary: Provide a concise summary of what this article is about, based on the title and context.

                 Priority: Estimate how important or relevant this article might be to a general tech-savvy user (low / medium / high). Use title content and comment volume as signals.

                 Time Estimate: Estimate how long it might take to read and understand the article  together with the comments (e.g., "2 min", "5-7 min").

                 Sentiment: Predict the likely sentiment of the article and its discussion (positive / negative / neutral). Use comment count and title tone as clues.

                 Respond in JSON format like:
                 {
                   "summary": "...",
                   "priority": "high",
                   "timeEstimate": "5-7 min",
                   "sentiment": "neutral"
                 }

                 Do not output backticks or markdown. Do not output anything except for Json itself!                
                 """;
        return builder
                .defaultSystem(system)
                .build();
    }

}
