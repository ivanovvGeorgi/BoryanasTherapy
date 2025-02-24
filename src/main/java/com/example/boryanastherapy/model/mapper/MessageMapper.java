package com.example.boryanastherapy.model.mapper;

import com.example.boryanastherapy.model.dto.MessageDTO;
import com.example.boryanastherapy.model.entity.Message;

import java.time.LocalDateTime;

public class MessageMapper {
    public static Message toEntity(MessageDTO messageDTO) {
        if (messageDTO == null) {
            return null;
        }

        Message message = new Message();
        message.setName(messageDTO.getName());
        message.setEmail(messageDTO.getEmail());
        message.setMessage(messageDTO.getMessage());
        message.setDate(LocalDateTime.now());
        message.setIsRead(false);
        return message;
    }
}
