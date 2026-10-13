package com.lsxuan.udilabel.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class AiChatClient {

    private final ChatClient chatClient;

    public AiChatClient(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String chat(String message) {
        return chatClient.prompt().user(message).call().content();
    }
}
