package com.chat.repositories;

import com.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
    
}
