package com.chat.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chat.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByMobileNumber(String mobileNumber);
	
	List<User> findByNameContainingIgnoreCase(String namePart);
}
