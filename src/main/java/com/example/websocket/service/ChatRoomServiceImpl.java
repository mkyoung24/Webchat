package com.example.websocket.service;

import com.example.websocket.dto.ChatRoom;
import com.example.websocket.entity.ChatRoomEntity;
import com.example.websocket.repository.ChatMessageRepository;
import com.example.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    private final ChatRoomRepository repository;

    private final ChatMessageRepository messageRepository;


    public List<ChatRoom> findAllRoom() {       //전체 채팅방 조회
        List<ChatRoomEntity> chatRoomEntities = repository.findAll();

        List<ChatRoom> chatRooms = new ArrayList<>();
        for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
            ChatRoom chatRoom = entityToDto(chatRoomEntity);
            chatRooms.add(chatRoom);
        }
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoomById(String roomId) {       //roomID 기준으로 채팅방 찾기
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom dto = entityToDto(entity);

        return dto;
    }

    public ChatRoom createChatRoom(String roomName, String roomPwd, boolean secretChk, int maxUserCnt) {       //roomName로 채팅방 만들기
        ChatRoomEntity entity = ChatRoomEntity.builder()
                .roomName(roomName)
                .roomPwd(roomPwd)
                .secretChk(secretChk)
                .userlist(new HashMap<String, String>())
                .userCount(0)
                .maxUserCnt(maxUserCnt)
                .build();

        repository.save(entity);

        ChatRoom chatRoom = entityToDto(entity);

        return chatRoom;

    }



    public boolean chkRoomUserCnt(String roomId) {      //채팅방 입장 제한 여부
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);


        if (chatRoom.getUserCount() + 1 > chatRoom.getMaxUserCnt()) {
            return false;
        }

        return true;
    }


    public boolean confirmPwd(String roomId, String roomPwd) {      //채팅방 비밀번호 조회
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);

        return roomPwd.equals(chatRoom.getRoomPwd());
    }

    public void delChatRoom(String roomId) {        //채팅방 삭제
        try {
            messageRepository.deleteChatMessageEntityByChatRoomId(roomId);
            repository.deleteById(roomId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void plusUserCnt(String roomId) {        //채팅방 인원+1
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);

        ChatRoomEntity roomEntity = ChatRoomEntity.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .roomPwd(chatRoom.getRoomPwd())
                .secretChk(chatRoom.isSecretChk())
                .userlist(chatRoom.getUserlist())
                .userCount(chatRoom.getUserCount()+1)
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .build();

        repository.save(roomEntity);
    }

    public void minusUsercnt(String roomId) {       //채팅방 인원-1
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);

        ChatRoomEntity roomEntity = ChatRoomEntity.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .roomPwd(chatRoom.getRoomPwd())
                .secretChk(chatRoom.isSecretChk())
                .userlist(chatRoom.getUserlist())
                .userCount(chatRoom.getUserCount()-1)
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .build();

        repository.save(roomEntity);
    }

    public String addUser(String roomId, String userName) {     //채팅방 유저 리스트에 유저 추가
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);

        String userUUID = UUID.randomUUID().toString();

        HashMap<String, String> newUserList = new HashMap<>(chatRoom.getUserlist());
        newUserList.put(userUUID, userName);

        ChatRoomEntity roomEntity = ChatRoomEntity.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .roomPwd(chatRoom.getRoomPwd())
                .secretChk(chatRoom.isSecretChk())
                .userlist(newUserList)
                .userCount(chatRoom.getUserCount())
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .build();

        repository.save(roomEntity);

        return userUUID;
    }

    public String isDuplicateName(String roomId, String username) {     //채팅방 유저 이름 중복 확인
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);
        String name = username;

        while(chatRoom.getUserlist().containsValue(name)) {
            int ranNum = (int) (Math.random()*100)+1;

            name = username + ranNum;
        }

        return name;
    }

    public void delUser(String roomId, String userUUID) {       //채팅방 유저 리스트 삭제
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        ChatRoom chatRoom = entityToDto(entity);

        HashMap<String, String> newUserList = new HashMap<>(chatRoom.getUserlist());
        newUserList.remove(userUUID);

        ChatRoomEntity roomEntity = ChatRoomEntity.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .roomPwd(chatRoom.getRoomPwd())
                .secretChk(chatRoom.isSecretChk())
                .userlist(newUserList)
                .userCount(chatRoom.getUserCount())
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .build();

        repository.save(roomEntity);

    }

    public String getUserName(String roomId, String userUUID) {     //채팅방 userName 조회
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);
        return chatRoom.getUserlist().get(userUUID);
    }

    public ArrayList<String> getUserList(String roomId) {       //채팅방 전체 userlist 조회
        ArrayList<String> list = new ArrayList<>();

        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );

        ChatRoom chatRoom = entityToDto(entity);

        HashMap<String, String> newUserList = new HashMap<>(chatRoom.getUserlist());
        newUserList.forEach((key, value) -> list.add(value));

        return list;

    }

    public void deleteUser(String roomId, String userName) {       //채팅방 유저 리스트 삭제
        ChatRoomEntity entity = repository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
        ChatRoom chatRoom = entityToDto(entity);

        HashMap<String, String> newUserList = new HashMap<>(chatRoom.getUserlist());
        String userUUID = null;
        for (Map.Entry<String, String> entry : newUserList.entrySet()) {
            if (entry.getValue().equals(userName)) {
                userUUID = entry.getKey();
                break;
            }
        }


        newUserList.remove(userUUID);

        ChatRoomEntity roomEntity = ChatRoomEntity.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .roomPwd(chatRoom.getRoomPwd())
                .secretChk(chatRoom.isSecretChk())
                .userlist(newUserList)
                .userCount(chatRoom.getUserCount())
                .maxUserCnt(chatRoom.getMaxUserCnt())
                .build();

        repository.save(roomEntity);

    }
}
