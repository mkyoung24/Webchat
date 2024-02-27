package com.example.websocket.controller;

import com.example.websocket.dto.ChatRoom;
import com.example.websocket.service.ChatRoomService;
import com.example.websocket.service.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatRoomController {

    private final ChatRoomServiceImpl service;

    @GetMapping("/")
    public String goChatRoom(Model model) {     //채팅방 리스트
        model.addAttribute("list", service.findAllRoom());
        log.info("SHOW ALL ChatList {}", service.findAllRoom());
        return "roomlist";
    }

    @PostMapping("/chat/createroom")
    public String createRoom(@RequestParam("roomName") String name, @RequestParam("roomPwd")String roomPwd, @RequestParam("secretChk")String secretChk,
                             @RequestParam(value = "maxUserCnt", defaultValue = "100")String maxUserCnt,  RedirectAttributes rttr) {      //채팅방 생성
        ChatRoom room = service.createChatRoom(name, roomPwd, Boolean.parseBoolean(secretChk), Integer.parseInt(maxUserCnt));
        log.info("CREATE Chat Room {}", room);
        rttr.addFlashAttribute("roomName", room);
        return "redirect:/";
    }

    @GetMapping("/chat/room")
    public String roomDetail(Model model, String roomId) {      //채팅방 입장
        log.info("roomInfo {}", service.findRoomById(roomId));
        model.addAttribute("room", service.findRoomById(roomId));
        return "chatroom";
    }

    @PostMapping("/chat/confirmPwd/{roomId}")
    @ResponseBody
    public boolean confirmPwd(@PathVariable String roomId, @RequestParam String roomPwd) {      //채팅방 비밀번호 확인
        return service.confirmPwd(roomId, roomPwd);
    }

    @GetMapping("/chat/delRoom/{roomId}")
    public String delChatRoom(@PathVariable String roomId) {        //채팅방 삭제
        service.delChatRoom(roomId);
        return "redirect:/";
    }

    @GetMapping("/chat/chkUserCnt/{roomId}")
    @ResponseBody
    public boolean chUserCnt(@PathVariable String roomId) {         //채팅방 입장 제한 여부

        return service.chkRoomUserCnt(roomId);
    }

}
