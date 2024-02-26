package com.example.websocket.repository;

import com.example.websocket.entity.ChatMessageEntity;
import com.example.websocket.entity.ChatRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {

    @Query("{'chatRoom.roomId' :  ?0}")
    List<ChatMessageEntity> findChatMessageEntitiesByChatRoomId(String chatRoomId);

    @Query("{'chatRoom.roomId' :  ?0}")
    void deleteChatMessageEntityByChatRoomId(String chatRoomId);




}
