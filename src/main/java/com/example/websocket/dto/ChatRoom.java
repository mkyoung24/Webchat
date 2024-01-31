package com.example.websocket.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
@Builder
public class ChatRoom {

    private String roomId;      //채팅방 아이디
    private String roomName;        //채팅방 이름
    private int userCount;     //채팅방 인원수

    private int maxUserCnt;     //채팅방 최대 인원 제한

    private String roomPwd;     //채팅방 삭제시 필요한 pwd

    private boolean secretChk;      //채팅방 잠금 여부

    private HashMap<String, String> userlist;


}
