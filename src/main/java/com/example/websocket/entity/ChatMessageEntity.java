package com.example.websocket.entity;
import com.example.websocket.dto.ChatMessage;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@Data
@Document(collection = "chatMessage")
public class ChatMessageEntity {

    @Id
    private String chatMessageId;      //채팅 아이디
    private ChatMessage.MessageType type;   //메시지 타입
    private String sender;      //채팅을 보낸 사람
    private String message;     //메시지
    @DBRef
    private ChatRoomEntity chatRoom;    //채팅방 번호

}
