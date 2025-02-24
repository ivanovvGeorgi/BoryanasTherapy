package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.Message;
import com.example.boryanastherapy.repository.MessageRepository;
import com.example.boryanastherapy.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(Message message) {
        this.messageRepository.save(message);
    }

    @Override
    public List<Message> getAllMessagesSortedByDate() {
        return messageRepository.findAllByOrderByDateDesc();  // Custom query to get messages sorted by date
    }

    @Override
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        message.setIsRead(true);  // Mark the message as read
        messageRepository.save(message);  // Save the updated message
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);  // This will remove the message from the database
    }
}
