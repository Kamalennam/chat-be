package com.chat.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "group_members")
public class GroupMember {

    @Id
    private String id;
    
    private String groupId;
    private String userId;
    private boolean isAdmin;
    private boolean isActive;
    
    @CreatedDate
    @Field("joined_at")
    private Instant joinedAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
    
    public GroupMember() {}
    
    public GroupMember(String groupId, String userId, boolean isAdmin) {
        this.groupId = groupId;
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.isActive = true;
        this.joinedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
} 