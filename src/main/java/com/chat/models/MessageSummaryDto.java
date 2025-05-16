
package com.chat.models;

import java.time.Instant;

public class MessageSummaryDto {
    private String text;
    private Instant timestamp;

    public MessageSummaryDto(String text, Instant timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
