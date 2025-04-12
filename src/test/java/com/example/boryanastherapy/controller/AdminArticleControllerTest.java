package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.entity.Article;
import com.example.boryanastherapy.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminArticleControllerTest {

    private MockMvc mockMvc;
    private ArticleService articleService;

    @BeforeEach
    void setup() {
        articleService = mock(ArticleService.class);
        AdminArticleController controller = new AdminArticleController(articleService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listArticles_shouldReturnListViewAndArticles() throws Exception {
        List<Article> articles = Arrays.asList(
                new Article("Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now()),
                new Article("Title 2", "Content 2", LocalDateTime.now(), LocalDateTime.now())
        );
        when(articleService.findAll()).thenReturn(articles);

        mockMvc.perform(get("/admin/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/articles/list"))
                .andExpect(model().attribute("articles", articles));
    }

    @Test
    void createArticleForm_shouldReturnCreateFormViewAndEmptyArticle() throws Exception {
        mockMvc.perform(get("/admin/articles/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/articles/create"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    void saveArticle_shouldSaveArticleAndRedirectToList() throws Exception {
        Article articleToSave = new Article("Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now());

        mockMvc.perform(post("/admin/articles/save")
                        .flashAttr("article", articleToSave)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/articles"))
                .andExpect(flash().attribute("success", "Article saved successfully"));

        verify(articleService, times(1)).save(articleToSave);
    }

    @Test
    void deleteArticle_shouldDeleteArticleAndRedirectToList() throws Exception {
        Long articleId = 1L;

        mockMvc.perform(post("/admin/articles/delete/" + articleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/articles"))
                .andExpect(flash().attribute("success", "Article deleted successfully"));

        verify(articleService, times(1)).deleteById(articleId);
    }

    @Test
    void uploadImage_shouldReturnErrorIfImageIsEmpty() throws Exception {
        MockMultipartFile emptyImage = new MockMultipartFile(
                "image", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]
        );

        mockMvc.perform(multipart("/admin/articles/upload-image")
                        .file(emptyImage))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }
}
