package com.chat.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "group_messages")
public class GroupMessage {

    @Id
    private String id;
    
    private String groupId;
    private String senderId;
    private String text;
    private String attachmentUrl;
    private boolean isDeleted;
    
    // For tracking message status per user: SENT, DELIVERED, SEEN
    private Map<String, String> messageStatus = new HashMap<>();
    
    @CreatedDate
    @Field("created_at")
    private Instant createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
    
    public GroupMessage() {}
    
    public GroupMessage(String groupId, String senderId, String text) {
        this.groupId = groupId;
        this.senderId = senderId;
        this.text = text;
        this.isDeleted = false;
        this.createdAt = Instant.now();
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Map<String, String> getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Map<String, String> messageStatus) {
        this.messageStatus = messageStatus;
    }
    
    public void updateUserMessageStatus(String userId, String status) {
        this.messageStatus.put(userId, status);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
} 