package com.example.websocket.controller;

import com.example.websocket.dto.ChatMessage;
import com.example.websocket.service.ChatMessageServiceImpl;
import com.example.websocket.service.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;       //메시지 전송 인터페이스

    private final ChatMessageServiceImpl messageService;

    private final ChatRoomServiceImpl roomService;

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatMessage chat, SimpMessageHeaderAccessor headerAccessor) {        //유저 입장
        roomService.plusUserCnt(chat.getRoomId());
        String userUUID = roomService.addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + "님 입장!!");
        messagingTemplate.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessage chat) {        //해당 유저 메시지 처리
        log.info("CHAT {}", chat);
        List<ChatMessage> messages = messageService.saveChat(chat);
        log.info("CHAT Messages {}", messages);
        //chat.setMessage(chat.getMessage());
        //messageService.saveChat(chat);
        messagingTemplate.convertAndSend("/sub/chat/room/" + chat.getRoomId(), messages);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {     //유저 퇴장
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        roomService.minusUsercnt(roomId);

        String username = roomService.getUserName(roomId, userUUID);
        roomService.delUser(roomId, userUUID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            ChatMessage chat = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, chat);
        }

    }

    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {      //채팅에 참여한 유저 리스트 반환

        return roomService.getUserList(roomId);
    }

    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {       //채팅에 참여한 유저 닉네임 중복 확인
        String userName = roomService.isDuplicateName(roomId, username);
        log.info("유저 이름 확인 {}", userName);

        return userName;
    }

}
