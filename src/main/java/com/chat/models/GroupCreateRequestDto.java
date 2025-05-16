package com.chat.models;

public class GroupCreateRequestDto {
    private String name;
    private String description;
    private String adminMobile;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAdminMobile() {
        return adminMobile;
    }
    
    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }
} 