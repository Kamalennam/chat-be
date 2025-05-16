package com.chat.repositories;

import com.chat.entity.GroupMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupMessageRepository extends MongoRepository<GroupMessage, String> {
    List<GroupMessage> findByGroupIdOrderByCreatedAtDesc(String groupId);
    List<GroupMessage> findByGroupIdAndIsDeletedFalseOrderByCreatedAtDesc(String groupId);
    List<GroupMessage> findByGroupIdAndSenderIdOrderByCreatedAtDesc(String groupId, String senderId);
} 