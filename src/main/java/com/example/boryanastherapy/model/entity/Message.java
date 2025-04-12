package com.example.boryanastherapy.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {
    private String name;
    private String email;
    private String message;
    private LocalDateTime date;
    private Boolean isRead;

    public Message(long id, String name, boolean isRead) {
        super(); // Assuming BaseEntity has a constructor accepting an id
        this.name = name;
        this.isRead = isRead;
        this.date = LocalDateTime.now(); // Defaulting date to current date/time
        this.message = ""; // Providing a default message (empty string)
        this.email = ""; // Providing a default email (empty string)
    }
}
