package com.example.websocket.service;

import com.example.websocket.dto.ChatRoom;
import com.example.websocket.entity.ChatRoomEntity;
import com.example.websocket.repository.ChatRoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


public interface ChatRoomService {

    default ChatRoom entityToDto(ChatRoomEntity entity) {
        HashMap<String, String> userlist = new HashMap<>();
        for (Map.Entry<String, String> entry : entity.getUserlist().entrySet()) {
            userlist.put(entry.getKey(), entry.getValue());
        }

        ChatRoom dto = ChatRoom.builder()
                .roomId(entity.getRoomId())
                .roomName(entity.getRoomName())
                .userCount(entity.getUserCount())
                .maxUserCnt(entity.getMaxUserCnt())
                .roomPwd(entity.getRoomPwd())
                .secretChk(entity.isSecretChk())
                .userlist(userlist)
                .build();

        return dto;
    }

    default ChatRoomEntity dtoToEntity(ChatRoom dto) {
        ChatRoomEntity entity = ChatRoomEntity.builder()
                .roomId(dto.getRoomId())
                .roomName(dto.getRoomName())
                .userCount(dto.getUserCount())
                .maxUserCnt(dto.getMaxUserCnt())
                .roomPwd(dto.getRoomPwd())
                .secretChk(dto.isSecretChk())
                .build();
        return  entity;
    }
}
