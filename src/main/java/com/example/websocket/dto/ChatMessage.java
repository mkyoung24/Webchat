package com.example.websocket.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, LEAVE;
    }
    private MessageType type;   //메시지 타입
    private String roomId;      //방 번호
    private String sender;      //채팅을 보낸 사람
    private String message;     //메시지

}
