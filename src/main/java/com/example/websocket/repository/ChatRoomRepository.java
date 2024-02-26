package com.example.websocket.repository;

import com.example.websocket.entity.ChatRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoomEntity, String> {


}
