package com.chat.chatAppln;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("com.chat")
@EnableMongoRepositories("com.chat.repositories")
@EnableMongoAuditing
public class ChatApplnApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplnApplication.class, args);
	}

}
