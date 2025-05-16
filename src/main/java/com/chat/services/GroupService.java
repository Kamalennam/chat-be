package com.chat.services;

import com.chat.entity.Group;
import com.chat.entity.GroupMember;
import com.chat.entity.User;
import com.chat.models.GenericResponse;
import com.chat.models.GroupCreateRequestDto;
import com.chat.models.GroupMemberRequestDto;
import com.chat.models.GroupMemberResponseDto;
import com.chat.models.GroupResponseDto;
import com.chat.repositories.GroupMemberRepository;
import com.chat.repositories.GroupRepository;
import com.chat.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    
    // In-memory storage for online status
    private final Map<String, String> userOnlineStatus = new HashMap<>();

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    public GenericResponse createGroup(GroupCreateRequestDto requestDto) {
        Optional<User> adminOpt = userRepository.findByMobileNumber(requestDto.getAdminMobile());
        
        if (adminOpt.isEmpty()) {
            return new GenericResponse("Admin user not found", "FAILURE");
        }
        
        User admin = adminOpt.get();
        
        Group group = new Group(requestDto.getName(), requestDto.getDescription(), admin.getId());
        group = groupRepository.save(group);
        
        // Add admin as the first member
        GroupMember adminMember = new GroupMember(group.getId(), admin.getId(), true);
        groupMemberRepository.save(adminMember);
        
        return new GenericResponse("Group created successfully with ID: " + group.getId(), "SUCCESS");
    }
    
    public List<GroupResponseDto> getGroupsForUser(String mobileNumber) {
        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);
        
        if (userOpt.isEmpty()) {
            return List.of();
        }
        
        User user = userOpt.get();
        
        // Find all groups where user is a member
        List<GroupMember> memberships = groupMemberRepository.findByUserIdAndIsActive(user.getId(), true);
        
        return memberships.stream()
                .map(member -> {
                    Optional<Group> groupOpt = groupRepository.findById(member.getGroupId());
                    if (groupOpt.isPresent()) {
                        Group group = groupOpt.get();
                        long memberCount = groupMemberRepository.countByGroupIdAndIsActive(group.getId(), true);
                        
                        // Get admin mobile number
                        String adminMobile = "";
                        Optional<User> adminOpt = userRepository.findById(group.getAdminId());
                        if (adminOpt.isPresent()) {
                            adminMobile = adminOpt.get().getMobileNumber();
                        }
                        
                        return new GroupResponseDto(
                                group.getId(),
                                group.getName(),
                                group.getDescription(),
                                adminMobile,
                                (int) memberCount,
                                group.getCreatedAt()
                        );
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
    
    public GenericResponse addMemberToGroup(GroupMemberRequestDto requestDto) {
        Optional<User> adminOpt = userRepository.findByMobileNumber(requestDto.getAdminMobile());
        Optional<User> memberOpt = userRepository.findByMobileNumber(requestDto.getMemberMobile());
        Optional<Group> groupOpt = groupRepository.findById(requestDto.getGroupId());
        
        if (adminOpt.isEmpty() || memberOpt.isEmpty() || groupOpt.isEmpty()) {
            return new GenericResponse("Admin, member, or group not found", "FAILURE");
        }
        
        User admin = adminOpt.get();
        User member = memberOpt.get();
        Group group = groupOpt.get();
        
        // Check if admin is actually the admin of the group
        if (!group.getAdminId().equals(admin.getId())) {
            return new GenericResponse("Only group admin can add members", "FAILURE");
        }
        
        // Check if member limit is reached
        long currentMemberCount = groupMemberRepository.countByGroupIdAndIsActive(group.getId(), true);
        if (currentMemberCount >= group.getMemberLimit()) {
            return new GenericResponse("Group member limit reached", "FAILURE");
        }
        
        // Check if member is already in the group
        Optional<GroupMember> existingMemberOpt = groupMemberRepository.findByGroupIdAndUserId(group.getId(), member.getId());
        
        if (existingMemberOpt.isPresent()) {
            GroupMember existingMember = existingMemberOpt.get();
            if (existingMember.isActive()) {
                return new GenericResponse("User is already a member of this group", "FAILURE");
            } else {
                // Reactivate the member
                existingMember.setActive(true);
                groupMemberRepository.save(existingMember);
                return new GenericResponse("Member added back to the group", "SUCCESS");
            }
        }
        
        // Add the new member
        GroupMember newMember = new GroupMember(group.getId(), member.getId(), false);
        groupMemberRepository.save(newMember);
        
        return new GenericResponse("Member added to the group successfully", "SUCCESS");
    }
    
    public GenericResponse removeMemberFromGroup(GroupMemberRequestDto requestDto) {
        Optional<User> adminOpt = userRepository.findByMobileNumber(requestDto.getAdminMobile());
        Optional<User> memberOpt = userRepository.findByMobileNumber(requestDto.getMemberMobile());
        Optional<Group> groupOpt = groupRepository.findById(requestDto.getGroupId());
        
        if (adminOpt.isEmpty() || memberOpt.isEmpty() || groupOpt.isEmpty()) {
            return new GenericResponse("Admin, member, or group not found", "FAILURE");
        }
        
        User admin = adminOpt.get();
        User member = memberOpt.get();
        Group group = groupOpt.get();
        
        // Check if admin is actually the admin of the group
        if (!group.getAdminId().equals(admin.getId())) {
            return new GenericResponse("Only group admin can remove members", "FAILURE");
        }
        
        // Find the member in the group
        Optional<GroupMember> membershipOpt = groupMemberRepository.findByGroupIdAndUserId(group.getId(), member.getId());
        
        if (membershipOpt.isEmpty() || !membershipOpt.get().isActive()) {
            return new GenericResponse("User is not an active member of this group", "FAILURE");
        }
        
        // Deactivate the member
        GroupMember membership = membershipOpt.get();
        membership.setActive(false);
        groupMemberRepository.save(membership);
        
        return new GenericResponse("Member removed from the group successfully", "SUCCESS");
    }
    
    public List<GroupMemberResponseDto> getGroupMembers(String groupId) {
        List<GroupMember> members = groupMemberRepository.findByGroupIdAndIsActive(groupId, true);
        
        return members.stream()
                .map(member -> {
                    Optional<User> userOpt = userRepository.findById(member.getUserId());
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        GroupMemberResponseDto dto = new GroupMemberResponseDto();
                        dto.setId(member.getId());
                        dto.setGroupId(member.getGroupId());
                        dto.setUserId(user.getId());
                        dto.setMobileNumber(user.getMobileNumber());
                        dto.setAdmin(member.isAdmin());
                        dto.setActive(member.isActive());
                        dto.setJoinedAt(member.getJoinedAt());
                        
                        // Set online status (online/offline)
                        String status = userOnlineStatus.getOrDefault(user.getId(), "OFFLINE");
                        dto.setOnlineStatus(status);
                        
                        return dto;
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
    
    public void updateUserStatus(String userId, String status) {
        userOnlineStatus.put(userId, status);
    }
    
    public boolean isUserGroupAdmin(String groupId, String userId) {
        return groupMemberRepository.existsByGroupIdAndUserIdAndIsAdmin(groupId, userId, true);
    }
} 