package com.chat.models;



import java.time.Instant;

public class UserDto {

    private String id;
    private String mobileNumber;
    private boolean isOnline;
    private Instant lastSeen;
    private Instant createdAt;
    private Instant updatedAt;

    public UserDto() {}

    public UserDto(String id, String mobileNumber, boolean isOnline, Instant lastSeen, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Convenience constructor to convert from entity
    public UserDto(com.chat.entity.User user) {
        this.id = user.getId();
        this.mobileNumber = user.getMobileNumber();
        this.isOnline = user.isOnline();
        this.lastSeen = user.getLastSeen();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Instant lastSeen) {
        this.lastSeen = lastSeen;
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
