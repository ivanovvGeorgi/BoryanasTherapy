package com.example.boryanastherapy.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MailConfig.class)
public class MailConfigTest {

    @Autowired
    private JavaMailSender mailSender; // Autowire the interface

    @Test
    void javaMailSenderBeanIsCreated() {
        assertNotNull(mailSender);
    }

    @Test
    void javaMailSenderBeanHasCorrectHost() {
        // You'll need to cast to JavaMailSenderImpl to access getHost()
        assertEquals("smtp.gmail.com", ((JavaMailSenderImpl) mailSender).getHost());
    }

    @Test
    void javaMailSenderBeanHasCorrectPort() {
        // You'll need to cast to JavaMailSenderImpl to access getPort()
        assertEquals(587, ((JavaMailSenderImpl) mailSender).getPort());
    }

    @Test
    void javaMailSenderBeanHasCorrectUsername() {
        // You'll need to cast to JavaMailSenderImpl to access getUsername()
        assertEquals("gojgo6@gmail.com", ((JavaMailSenderImpl) mailSender).getUsername());
    }

    @Test
    void javaMailSenderBeanHasCorrectAuthProperty() {
        // You'll need to cast to JavaMailSenderImpl to access getJavaMailProperties()
        assertEquals("true", ((JavaMailSenderImpl) mailSender).getJavaMailProperties().getProperty("mail.smtp.auth"));
    }

    @Test
    void javaMailSenderBeanHasCorrectStartTlsProperty() {
        // You'll need to cast to JavaMailSenderImpl to access getJavaMailProperties()
        assertEquals("true", ((JavaMailSenderImpl) mailSender).getJavaMailProperties().getProperty("mail.smtp.starttls.enable"));
    }
}