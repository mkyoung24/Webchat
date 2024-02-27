package com.example.websocket.service;

import com.example.websocket.dto.ChatMessage;
import com.example.websocket.dto.ChatRoom;
import com.example.websocket.entity.ChatMessageEntity;
import com.example.websocket.entity.ChatRoomEntity;
import com.example.websocket.repository.ChatMessageRepository;
import com.example.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.*;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService{


    private final ChatMessageRepository messageRepository;

    private final ChatRoomRepository roomRepository;

    public List<ChatMessage> saveChat(ChatMessage chat) {
        ChatRoomEntity chatRoom = roomRepository.findById(chat.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatMessageEntity entity = ChatMessageEntity.builder()
                .type(chat.getType())
                .message(chat.getMessage())
                .sender(chat.getSender())
                .chatRoom(chatRoom)
                .build();

        messageRepository.save(entity);

        List<ChatMessageEntity> chatMessageEntities = messageRepository.findChatMessageEntitiesByChatRoomId(chatRoom.getRoomId());

        List<ChatMessage> chatMessages = new ArrayList<>();
        for (ChatMessageEntity chatMessageEntity : chatMessageEntities) {
            ChatMessage chatMessage = entityToDto(chatMessageEntity);
            chatMessages.add(chatMessage);
        }

        return chatMessages;
    }

}