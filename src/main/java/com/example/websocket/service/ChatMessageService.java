package com.example.websocket.service;

import com.example.websocket.dto.ChatMessage;
import com.example.websocket.dto.ChatRoom;
import com.example.websocket.entity.ChatMessageEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public interface ChatMessageService {

    default ChatMessage entityToDto(ChatMessageEntity entity) {
        ChatMessage dto = ChatMessage.builder()
                .chatMessageId(entity.getChatMessageId())
                .type(entity.getType())
                .message(entity.getMessage())
                .sender(entity.getSender())
                .roomId(entity.getChatRoom().getRoomId())
                .build();
        return dto;
    }

}
