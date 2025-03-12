package com.example.boryanastherapy.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
public class Article extends BaseEntity{

    private String title;
    @Column(columnDefinition = "TEXT") // For longer content
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}