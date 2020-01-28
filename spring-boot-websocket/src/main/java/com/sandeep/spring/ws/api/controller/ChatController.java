package com.sandeep.spring.ws.api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.sandeep.spring.ws.api.model.Message;

@Controller
public class ChatController {
	
	@MessageMapping("/chat.register")
	@SendTo("/topic/public")
	public Message register(@Payload Message Message, SimpMessageHeaderAccessor headerAccessor) {
          headerAccessor.getSessionAttributes().put("username", Message.getSender());
          return Message;
}
	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public Message sendMessage(@Payload Message message) {
		return message;
	}
}