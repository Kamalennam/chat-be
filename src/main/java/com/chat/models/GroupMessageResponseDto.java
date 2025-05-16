package com.chat.models;

import java.time.Instant;
import java.util.Map;

public class GroupMessageResponseDto {
    private String id;
    private String groupId;
    private String senderMobile;
    private String senderName;
    private String text;
    private String attachmentUrl;
    private Map<String, String> messageStatus;
    private boolean isDeleted;
    private Instant timestamp;
    
    public GroupMessageResponseDto() {}

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

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public Map<String, String> getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Map<String, String> messageStatus) {
        this.messageStatus = messageStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
} 