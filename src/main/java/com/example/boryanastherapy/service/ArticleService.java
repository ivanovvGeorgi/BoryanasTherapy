package com.example.boryanastherapy.service;

import com.example.boryanastherapy.model.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> findAll();
    Article findById(Long id);
    Article save(Article article);
    void deleteById(Long id);
}