package com.alura.springai.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Todo o histórico é reenviado ao modelo a cada mensagem: histórico que só
 * cresce é custo que só cresce, até estourar a janela de contexto. Por isso a
 * janela é fixa — guardamos as N últimas mensagens e descartamos o resto.
 */
@Configuration
public class MemoriaConfig {

    public static final int TAMANHO_DA_JANELA = 10;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(TAMANHO_DA_JANELA)
                .build();
    }
}
