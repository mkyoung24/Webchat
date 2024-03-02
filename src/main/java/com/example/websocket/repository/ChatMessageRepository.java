package com.example.websocket.repository;

import com.example.websocket.entity.ChatMessageEntity;
import com.example.websocket.entity.ChatRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {

    @Query(value = "{'chatRoom.roomId' :  ?0}")
    List<ChatMessageEntity> findChatMessageEntitiyByChatRoomId(String chatRoomId);

    @Query(value = "{'chatRoom.roomId' :  ?0}", delete = true)
    void deleteChatMessageEntityByChatRoomId(String chatRoomId);




}
