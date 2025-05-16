package com.chat.controllers;
import com.chat.models.MessageRequestDto;
import com.chat.models.MessageSummaryDto;
import com.chat.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatWebSocketController {

    private final MessageService messageService;

    public ChatWebSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/send") // corresponds to /app/send from client
    @SendTo("/topic/messages") // clients listening to this will get the message
    public MessageSummaryDto sendMessage(MessageRequestDto message) {
        messageService.sendPrivateMessage(message); // persist it as usual
        return new MessageSummaryDto(message.getMessageText(), Instant.now()); // send to subscribers
    }
}

