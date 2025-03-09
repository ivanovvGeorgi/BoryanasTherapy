package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.Article;
import com.example.boryanastherapy.repository.ArticleRepository;
import com.example.boryanastherapy.service.ArticleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public Article save(Article article) {
        if (article.getId() == null) {
            article.setCreatedAt(LocalDateTime.now());
        } else {
            article.setUpdatedAt(LocalDateTime.now());
        }
        return articleRepository.save(article);
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
}