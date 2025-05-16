package com.chat.repositories;

import com.chat.entity.GroupMember;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends MongoRepository<GroupMember, String> {
    List<GroupMember> findByGroupId(String groupId);
    List<GroupMember> findByUserId(String userId);
    List<GroupMember> findByUserIdAndIsActive(String userId, boolean isActive);
    List<GroupMember> findByGroupIdAndIsActive(String groupId, boolean isActive);
    Optional<GroupMember> findByGroupIdAndUserId(String groupId, String userId);
    long countByGroupIdAndIsActive(String groupId, boolean isActive);
    boolean existsByGroupIdAndUserIdAndIsAdmin(String groupId, String userId, boolean isAdmin);
} 