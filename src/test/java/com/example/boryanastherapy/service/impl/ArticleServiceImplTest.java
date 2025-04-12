package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.Article;
import com.example.boryanastherapy.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceImplTest {

    private ArticleRepository articleRepository;
    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        articleService = new ArticleServiceImpl(articleRepository);
    }

    @Test
    void findAllShouldReturnAllArticles() {
        Article article1 = new Article();
        Article article2 = new Article();
        when(articleRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        List<Article> result = articleService.findAll();

        assertEquals(2, result.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void findByIdShouldReturnArticleWhenFound() {
        Article article = new Article();
        article.setId(1L);
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Article result = articleService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(articleRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdShouldReturnNullWhenNotFound() {
        when(articleRepository.findById(2L)).thenReturn(Optional.empty());

        Article result = articleService.findById(2L);

        assertNull(result);
        verify(articleRepository, times(1)).findById(2L);
    }

    @Test
    void saveShouldSetCreatedAtForNewArticle() {
        Article newArticle = new Article(); // No ID -> new
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Article saved = articleService.save(newArticle);

        assertNotNull(saved.getCreatedAt());
        assertNull(saved.getUpdatedAt());
        verify(articleRepository, times(1)).save(newArticle);
    }

    @Test
    void saveShouldSetUpdatedAtForExistingArticle() {
        Article existing = new Article();
        existing.setId(5L); // Existing article
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Article saved = articleService.save(existing);

        assertNotNull(saved.getUpdatedAt());
        assertNull(saved.getCreatedAt());
        verify(articleRepository, times(1)).save(existing);
    }

    @Test
    void deleteByIdShouldCallRepository() {
        articleService.deleteById(3L);

        verify(articleRepository, times(1)).deleteById(3L);
    }
}
