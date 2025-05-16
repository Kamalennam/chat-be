
package com.chat.services;

import com.chat.entity.Chat;
import com.chat.entity.Message;
import com.chat.entity.User;
import com.chat.models.MessageRequestDto;
import com.chat.models.GenericResponse;
import com.chat.repositories.ChatRepository; 
import com.chat.repositories.MessageRepository;
import com.chat.repositories.UserRepository;

import java.time.Instant;
import java.util.Optional;

public class SendPrivateMessage {
    
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    
    public SendPrivateMessage(UserRepository userRepository, MessageRepository messageRepository, ChatRepository chatRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public GenericResponse sendPrivateMessage(MessageRequestDto request) {
        Optional<User> senderOpt = userRepository.findByMobileNumber(request.getSenderMobile());
        Optional<User> receiverOpt = userRepository.findByMobileNumber(request.getReceiverMobile());

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return new GenericResponse("Sender or Receiver not found", "FAILURE");
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();

        Message message = new Message();
        message.setSenderId(sender.getId());
        message.setReceiverId(receiver.getId());
        message.setText(request.getMessageText());
        message.setTimestamp(Instant.now());

        message = messageRepository.save(message);

        Chat chat = new Chat();
        chat.setSenderId(sender.getId());
        chat.setReceiverId(receiver.getId());
        chat.setMessageId(message.getId());
        chat.setTimestamp(Instant.now());

        chatRepository.save(chat);

        return new GenericResponse("Message sent successfully", "SUCCESS");
    }
}