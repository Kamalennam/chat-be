package com.chat.models;

import java.time.Instant;

public class GroupResponseDto {
    private String id;
    private String name;
    private String description;
    private String adminMobile;
    private int memberCount;
    private Instant createdAt;
    
    public GroupResponseDto() {}
    
    public GroupResponseDto(String id, String name, String description, String adminMobile, int memberCount, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adminMobile = adminMobile;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
} 