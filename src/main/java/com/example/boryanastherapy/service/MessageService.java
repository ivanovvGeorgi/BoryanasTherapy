package com.example.boryanastherapy.service;

import com.example.boryanastherapy.model.entity.Message;

import java.util.List;


public interface MessageService {
    void saveMessage(Message message);

     List<Message> getAllMessagesSortedByDate();

    void markAsRead(Long id);

     void deleteMessage(Long messageId);
    }


