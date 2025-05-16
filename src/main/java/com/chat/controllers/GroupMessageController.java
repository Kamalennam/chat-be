package com.chat.controllers;

import com.chat.models.GenericResponse;
import com.chat.models.GroupMessageRequestDto;
import com.chat.models.GroupMessageResponseDto;
import com.chat.services.GroupMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-messages")
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    public GroupMessageController(GroupMessageService groupMessageService) {
        this.groupMessageService = groupMessageService;
    }

    @PostMapping("/send")
    public ResponseEntity<GenericResponse> sendGroupMessage(@RequestBody GroupMessageRequestDto requestDto) {
        return ResponseEntity.ok(groupMessageService.sendGroupMessage(requestDto));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupMessageResponseDto>> getGroupMessages(@PathVariable String groupId) {
        return ResponseEntity.ok(groupMessageService.getGroupMessages(groupId));
    }

    @PostMapping("/{messageId}/status")
    public ResponseEntity<GenericResponse> updateMessageStatus(
            @PathVariable String messageId,
            @RequestParam String userId,
            @RequestParam String status) {
        return ResponseEntity.ok(groupMessageService.updateMessageStatus(messageId, userId, status));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<GenericResponse> deleteMessage(
            @PathVariable String messageId,
            @RequestParam String adminMobile) {
        return ResponseEntity.ok(groupMessageService.deleteMessage(messageId, adminMobile));
    }
} 