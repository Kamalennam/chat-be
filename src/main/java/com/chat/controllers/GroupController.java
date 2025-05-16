package com.chat.controllers;

import com.chat.models.GenericResponse;
import com.chat.models.GroupCreateRequestDto;
import com.chat.models.GroupMemberRequestDto;
import com.chat.models.GroupMemberResponseDto;
import com.chat.models.GroupResponseDto;
import com.chat.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createGroup(@RequestBody GroupCreateRequestDto requestDto) {
        return ResponseEntity.ok(groupService.createGroup(requestDto));
    }

    @GetMapping("/user/{mobileNumber}")
    public ResponseEntity<List<GroupResponseDto>> getUserGroups(@PathVariable String mobileNumber) {
        return ResponseEntity.ok(groupService.getGroupsForUser(mobileNumber));
    }

    @PostMapping("/members/add")
    public ResponseEntity<GenericResponse> addMemberToGroup(@RequestBody GroupMemberRequestDto requestDto) {
        return ResponseEntity.ok(groupService.addMemberToGroup(requestDto));
    }

    @PostMapping("/members/remove")
    public ResponseEntity<GenericResponse> removeMemberFromGroup(@RequestBody GroupMemberRequestDto requestDto) {
        return ResponseEntity.ok(groupService.removeMemberFromGroup(requestDto));
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponseDto>> getGroupMembers(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroupMembers(groupId));
    }
    
    @PostMapping("/users/{userId}/status/{status}")
    public ResponseEntity<GenericResponse> updateUserStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        groupService.updateUserStatus(userId, status);
        return ResponseEntity.ok(new GenericResponse("User status updated", "SUCCESS"));
    }
} 