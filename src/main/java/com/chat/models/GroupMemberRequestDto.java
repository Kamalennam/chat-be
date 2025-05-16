package com.chat.models;

public class GroupMemberRequestDto {
    private String groupId;
    private String adminMobile;
    private String memberMobile;
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getAdminMobile() {
        return adminMobile;
    }
    
    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }
    
    public String getMemberMobile() {
        return memberMobile;
    }
    
    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }
} 