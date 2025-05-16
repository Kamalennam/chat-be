package com.chat.services;

import com.chat.entity.Chat;
import com.chat.entity.Message;
import com.chat.entity.User;
import com.chat.models.MessageRequestDto;
import com.chat.models.MessageSummaryDto;
import com.chat.models.GenericResponse;
import com.chat.repositories.ChatRepository;
import com.chat.repositories.MessageRepository;
import com.chat.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public MessageService(UserRepository userRepository, MessageRepository messageRepository, ChatRepository chatRepository) {
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
        if (request.getSenderMobile().equals(request.getReceiverMobile())) {
            return new GenericResponse("Sender and Receiver cannot be the same", "FAILURE");
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
    
    
    public List<MessageSummaryDto> getChatMessages(String senderMobile, String receiverMobile) {
        Optional<User> senderOpt = userRepository.findByMobileNumber(senderMobile);
        Optional<User> receiverOpt = userRepository.findByMobileNumber(receiverMobile);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return List.of();
        }

        String senderId = senderOpt.get().getId();
        String receiverId = receiverOpt.get().getId();

        List<Message> messages = messageRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(senderId, receiverId, senderId, receiverId);

        return messages.stream()
                .map(msg -> new MessageSummaryDto(msg.getText(), msg.getTimestamp()))
                .toList();
    }

	
}
