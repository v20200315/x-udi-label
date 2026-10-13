package com.lsxuan.udilabel.infrastructure.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AiChatClientTest {

    @Autowired
    private AiChatClient aiChatClient;

    @Test
    void chat() {
        String response = aiChatClient.chat("你好，请介绍一下你自己。");

        System.out.println("========== AI Response ==========");
        System.out.println(response);
        System.out.println("=================================");

        assertNotNull(response);
        assertFalse(response.isBlank());
    }
}