package com.example.websocket.controller;

import com.example.websocket.dto.ChatRoom;
import com.example.websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatRoomController {

    private final ChatRoomRepository repository;

    @GetMapping("/")
    public String goChatRoom(Model model) {     //채팅 리스트 확인
        model.addAttribute("list", repository.findAllRoom());
        log.info("SHOW ALL ChatList {}", repository.findAllRoom());
        return "roomlist";
    }

    @PostMapping("/chat/createroom")
    public String createRoom(@RequestParam("roomName") String name, @RequestParam("roomPwd")String roomPwd, @RequestParam("secretChk")String secretChk,
                             @RequestParam(value = "maxUserCnt", defaultValue = "100")String maxUserCnt,  RedirectAttributes rttr) {      //채팅방 생성
        ChatRoom room = repository.createChatRoom(name, roomPwd, Boolean.parseBoolean(secretChk), Integer.parseInt(maxUserCnt));
        log.info("CREATE Chat Room {}", room);
        rttr.addFlashAttribute("roomName", room);
        return "redirect:/";
    }

    @GetMapping("/chat/room")
    public String roomDetail(Model model, String roomId) {      //채팅방 입장
        log.info("roomId {}", roomId);
        log.info("roomInfo {}", repository.findRoomById(roomId));
        model.addAttribute("room", repository.findRoomById(roomId));
        return "chatroom";
    }

    @PostMapping("/chat/confirmPwd/{roomId}")
    @ResponseBody
    public boolean confirmPwd(@PathVariable String roomId, @RequestParam String roomPwd) {      //채팅방 비밀번호 확인
        return repository.confirmPwd(roomId, roomPwd);
    }

    @GetMapping("/chat/delRoom/{roomId}")
    public String delChatRoom(@PathVariable String roomId) {        //채팅방 삭제
        repository.delChatRoom(roomId);
        return "redirect:/";
    }

    @GetMapping("/chat/chkUserCnt/{roomId}")
    @ResponseBody
    public boolean chUserCnt(@PathVariable String roomId) {         //채팅방 입장 제한 여부
        return repository.chkRoomUserCnt(roomId);
    }

}
