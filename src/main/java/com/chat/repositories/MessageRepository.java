package com.chat.repositories;

import com.chat.entity.Message;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    
	List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(String senderId, String receiverId, String receiverId2, String senderId2);

	

}
