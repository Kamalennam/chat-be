package com.chat.models;

public class GroupMessageRequestDto {
    private String groupId;
    private String senderMobile;
    private String messageText;
    private String attachmentUrl;
    
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
    
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    
    public String getAttachmentUrl() {
        return attachmentUrl;
    }
    
    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
} 