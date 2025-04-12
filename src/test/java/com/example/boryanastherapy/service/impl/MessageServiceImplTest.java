package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.Message;
import com.example.boryanastherapy.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;  // Mock the MessageRepository

    @InjectMocks
    private MessageServiceImpl messageService;  // Inject mock into the service class

    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mocks
        message = new Message();
        message.setId(1L);
        message.setMessage("Test message");
        message.setIsRead(false);
    }

    @Test
    void testSaveMessage() {
        // Call the method to test
        messageService.saveMessage(message);

        // Verify that save method is called on the repository
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testGetAllMessagesSortedByDate() {
        List<Message> expectedMessages = List.of(message);
        when(messageRepository.findAllByOrderByDateDesc()).thenReturn(expectedMessages);

        List<Message> result = messageService.getAllMessagesSortedByDate();

        // Verify the returned list of messages
        assertEquals(expectedMessages, result);
        verify(messageRepository, times(1)).findAllByOrderByDateDesc();  // Ensure the repository method is called
    }

    @Test
    void testMarkAsRead() {
        // Mock the repository to return a message when finding by ID
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        // Call the method to mark the message as read
        messageService.markAsRead(1L);

        // Verify that the message's 'isRead' field is set to true
        assertTrue(message.getIsRead());
        verify(messageRepository, times(1)).save(message);  // Ensure the repository save method was called
    }

    @Test
    void testMarkAsRead_messageNotFound() {
        // Mock repository to throw exception when message is not found
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method and expect an exception
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            messageService.markAsRead(1L);
        });

        // Verify the exception message
        assertEquals("Message not found", thrown.getMessage());
    }

    @Test
    void testDeleteMessage() {
        // Call the method to delete the message
        messageService.deleteMessage(1L);

        // Verify that deleteById was called with the correct ID
        verify(messageRepository, times(1)).deleteById(1L);
    }
}
