package com.chat.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chat.models.GenericResponse;
import com.chat.models.MessageRequestDto;
import com.chat.models.MessageSummaryDto;
import com.chat.services.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	 private final MessageService messageService;

	    public MessageController(MessageService messageService) {
	        this.messageService = messageService;
	    }

//	    @PostMapping("/personal-chat")
//	    public ResponseEntity<GenericResponse> sendMessage(@RequestBody MessageRequestDto request) {
//	        GenericResponse response = messageService.sendPrivateMessage(request);
//	        return ResponseEntity.ok(response);
//	    }
	    @PostMapping(value = "/personal-chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<GenericResponse> sendMessage(
	        @RequestParam("senderMobile") String senderMobile,
	        @RequestParam("receiverMobile") String receiverMobile,
	        @RequestParam(value = "messageText", required = false) String messageText,
	        @RequestParam(value = "file", required = false) MultipartFile file
	    ) {
	        GenericResponse response = messageService.sendPrivateMessage(senderMobile, receiverMobile, messageText, file);
	        return ResponseEntity.ok(response);
	    }

	    
	    @GetMapping("/chat")
	    public ResponseEntity<List<MessageSummaryDto>> getChatMessages(
	            @RequestParam String senderMobile,
	            @RequestParam String receiverMobile) {

	        List<MessageSummaryDto> messages = messageService.getChatMessages(senderMobile, receiverMobile);
	        return ResponseEntity.ok(messages);
	    }
	    
}
