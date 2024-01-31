package com.example.websocket.repository;

import com.example.websocket.dto.ChatRoom;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {       //전체 채팅방 조회
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoomById(String roomId) {       //roomID 기준으로 채팅방 찾기

        return chatRoomMap.get(roomId);
    }

    public ChatRoom createChatRoom(String roomName, String roomPwd, boolean secretChk, int maxUserCnt) {       //roomName로 채팅방 만들기
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .roomPwd(roomPwd)
                .secretChk(secretChk)
                .userlist(new HashMap<String, String>())
                .userCount(0)
                .maxUserCnt(maxUserCnt)
                .build();

        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);

        return chatRoom;

    }

    public void plusUserCnt(String roomId) {        //채팅방 인원+1
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount()+1);
    }

    public void minusUsercnt(String roomId) {       //채팅방 인원-1
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount()-1);
    }

    public boolean chkRoomUserCnt(String roomId) {      //채팅방 입장 제한 여부
        ChatRoom chatRoom = chatRoomMap.get(roomId);

        if (chatRoom.getUserCount() + 1 > chatRoom.getMaxUserCnt()) {
            return false;
        }

        return true;
    }

    public String addUser(String roomId, String userName) {     //채팅방 유저 리스트에 유저 추가
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        chatRoom.getUserlist().put(userUUID, userName);

        return userUUID;
    }

    public String isDuplicateName(String roomId, String username) {     //채팅방 유저 이름 중복 확인
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        String name = username;

        while(chatRoom.getUserlist().containsValue(name)) {
            int ranNum = (int) (Math.random()*100)+1;

            name = username + ranNum;
        }

        return name;
    }

    public void delUser(String roomId, String userUUID) {       //채팅방 유저 리스트 삭제
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        chatRoom.getUserlist().remove(userUUID);
    }

    public String getUserName(String roomId, String userUUID) {     //채팅방 userName 조회
        ChatRoom chatRoom = chatRoomMap.get(roomId);
        return chatRoom.getUserlist().get(userUUID);
    }

    public ArrayList<String> getUserList(String roomId) {       //채팅방 전체 userlist 조회
        ArrayList<String> list = new ArrayList<>();

        ChatRoom chatRoom = chatRoomMap.get(roomId);

        chatRoom.getUserlist().forEach((key, value) -> list.add(value));
        return list;
    }

    public boolean confirmPwd(String roomId, String roomPwd) {      //채팅방 비밀번호 조회
        return roomPwd.equals(chatRoomMap.get(roomId).getRoomPwd());
    }

    public void delChatRoom(String roomId) {        //채팅방 삭제
        try {
            chatRoomMap.remove(roomId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
