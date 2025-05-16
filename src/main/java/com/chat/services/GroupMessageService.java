package com.chat.services;

import com.chat.entity.GroupMessage;
import com.chat.entity.GroupMember;
import com.chat.entity.User;
import com.chat.models.GenericResponse;
import com.chat.models.GroupMessageRequestDto;
import com.chat.models.GroupMessageResponseDto;
import com.chat.repositories.GroupMemberRepository;
import com.chat.repositories.GroupMessageRepository;
import com.chat.repositories.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupMessageService {

    private final GroupMessageRepository groupMessageRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupMessageService(
            GroupMessageRepository groupMessageRepository,
            GroupMemberRepository groupMemberRepository,
            UserRepository userRepository,
            GroupService groupService,
            SimpMessagingTemplate messagingTemplate) {
        this.groupMessageRepository = groupMessageRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
        this.groupService = groupService;
        this.messagingTemplate = messagingTemplate;
    }

    public GenericResponse sendGroupMessage(GroupMessageRequestDto requestDto) {
        Optional<User> senderOpt = userRepository.findByMobileNumber(requestDto.getSenderMobile());
        
        if (senderOpt.isEmpty()) {
            return new GenericResponse("Sender not found", "FAILURE");
        }
        
        User sender = senderOpt.get();
        
        // Check if sender is a member of the group
        Optional<GroupMember> memberOpt = groupMemberRepository.findByGroupIdAndUserId(
                requestDto.getGroupId(), sender.getId());
        
        if (memberOpt.isEmpty() || !memberOpt.get().isActive()) {
            return new GenericResponse("User is not an active member of this group", "FAILURE");
        }
        
        // Create and save the message
        GroupMessage message = new GroupMessage(requestDto.getGroupId(), sender.getId(), requestDto.getMessageText());
        
        if (requestDto.getAttachmentUrl() != null && !requestDto.getAttachmentUrl().isEmpty()) {
            message.setAttachmentUrl(requestDto.getAttachmentUrl());
        }
        
        // Set initial status as SENT for all active group members
        List<GroupMember> activeMembers = groupMemberRepository.findByGroupIdAndIsActive(requestDto.getGroupId(), true);
        Map<String, String> initialStatus = new HashMap<>();
        
        for (GroupMember member : activeMembers) {
            if (member.getUserId().equals(sender.getId())) {
                initialStatus.put(member.getUserId(), "SEEN"); // Sender has seen their own message
            } else {
                initialStatus.put(member.getUserId(), "SENT"); // Default status for others
            }
        }
        
        message.setMessageStatus(initialStatus);
        message = groupMessageRepository.save(message);
        
        // Create response DTO for WebSocket broadcast
        GroupMessageResponseDto responseDto = convertToResponseDto(message);
        
        // Broadcast to the group's WebSocket topic
        messagingTemplate.convertAndSend("/topic/group/" + requestDto.getGroupId(), responseDto);
        
        return new GenericResponse("Message sent successfully", "SUCCESS");
    }
    
    public List<GroupMessageResponseDto> getGroupMessages(String groupId) {
        List<GroupMessage> messages = groupMessageRepository.findByGroupIdAndIsDeletedFalseOrderByCreatedAtDesc(groupId);
        
        return messages.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public GenericResponse updateMessageStatus(String messageId, String userId, String status) {
        Optional<GroupMessage> messageOpt = groupMessageRepository.findById(messageId);
        
        if (messageOpt.isEmpty()) {
            return new GenericResponse("Message not found", "FAILURE");
        }
        
        GroupMessage message = messageOpt.get();
        message.updateUserMessageStatus(userId, status);
        groupMessageRepository.save(message);
        
        // Create response DTO for WebSocket broadcast of status update
        GroupMessageResponseDto responseDto = convertToResponseDto(message);
        
        // Broadcast status update to the group's WebSocket topic
        messagingTemplate.convertAndSend("/topic/group/" + message.getGroupId() + "/status", responseDto);
        
        return new GenericResponse("Message status updated", "SUCCESS");
    }
    
    public GenericResponse deleteMessage(String messageId, String adminMobile) {
        Optional<User> adminOpt = userRepository.findByMobileNumber(adminMobile);
        Optional<GroupMessage> messageOpt = groupMessageRepository.findById(messageId);
        
        if (adminOpt.isEmpty() || messageOpt.isEmpty()) {
            return new GenericResponse("Admin or message not found", "FAILURE");
        }
        
        User admin = adminOpt.get();
        GroupMessage message = messageOpt.get();
        
        // Check if admin is actually the admin of the group
        if (!groupService.isUserGroupAdmin(message.getGroupId(), admin.getId())) {
            return new GenericResponse("Only group admin can delete messages", "FAILURE");
        }
        
        message.setDeleted(true);
        groupMessageRepository.save(message);
        
        // Create response DTO for WebSocket broadcast of deletion
        GroupMessageResponseDto responseDto = convertToResponseDto(message);
        
        // Broadcast deletion to the group's WebSocket topic
        messagingTemplate.convertAndSend("/topic/group/" + message.getGroupId() + "/delete", responseDto);
        
        return new GenericResponse("Message deleted successfully", "SUCCESS");
    }
    
    private GroupMessageResponseDto convertToResponseDto(GroupMessage message) {
        GroupMessageResponseDto dto = new GroupMessageResponseDto();
        dto.setId(message.getId());
        dto.setGroupId(message.getGroupId());
        dto.setText(message.getText());
        dto.setAttachmentUrl(message.getAttachmentUrl());
        dto.setDeleted(message.isDeleted());
        dto.setMessageStatus(message.getMessageStatus());
        dto.setTimestamp(message.getCreatedAt());
        
        // Get sender details
        Optional<User> senderOpt = userRepository.findById(message.getSenderId());
        if (senderOpt.isPresent()) {
            User sender = senderOpt.get();
            dto.setSenderMobile(sender.getMobileNumber());
            dto.setSenderName(sender.getMobileNumber()); // Using mobile as name since we don't have names
        }
        
        return dto;
    }
} 