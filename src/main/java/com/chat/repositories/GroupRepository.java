package com.chat.repositories;

import com.chat.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findByAdminId(String adminId);
    Optional<Group> findByIdAndAdminId(String id, String adminId);
} 