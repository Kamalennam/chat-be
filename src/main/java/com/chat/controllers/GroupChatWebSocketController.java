package com.chat.controllers;

import com.chat.models.GroupMessageRequestDto;
import com.chat.models.GroupMessageResponseDto;
import com.chat.services.GroupMessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GroupChatWebSocketController {

    private final GroupMessageService groupMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupChatWebSocketController(GroupMessageService groupMessageService, SimpMessagingTemplate messagingTemplate) {
        this.groupMessageService = groupMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/group/{groupId}/send")
    @SendTo("/topic/group/{groupId}")
    public GroupMessageResponseDto sendGroupMessage(
            @DestinationVariable String groupId,
            GroupMessageRequestDto message) {
        // Set the groupId from the path variable
        message.setGroupId(groupId);
        // Send and save the message
        groupMessageService.sendGroupMessage(message);
        
        // This will be broadcast to all subscribers of the group topic
        return null; // actual broadcasting happens in the service
    }
    
    @MessageMapping("/group/{groupId}/status/{messageId}")
    public void updateMessageStatus(
            @DestinationVariable String groupId,
            @DestinationVariable String messageId,
            String payload) {
        // Format expected: userId:status (e.g. "123:SEEN")
        String[] parts = payload.split(":");
        if (parts.length == 2) {
            String userId = parts[0];
            String status = parts[1];
            groupMessageService.updateMessageStatus(messageId, userId, status);
        }
    }
    
    @MessageMapping("/user/{userId}/status")
    public void updateUserStatus(@DestinationVariable String userId, String status) {
        // Broadcast user status change to appropriate topics
        messagingTemplate.convertAndSend("/topic/users/status", userId + ":" + status);
    }
} 