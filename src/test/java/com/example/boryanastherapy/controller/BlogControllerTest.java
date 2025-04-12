package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.entity.Article;
import com.example.boryanastherapy.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BlogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private BlogController blogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Here, you can configure MockMvc to avoid circular view path issues
        mockMvc = MockMvcBuilders.standaloneSetup(blogController)
                .setViewResolvers((viewName, locale) -> {
                    if ("blog".equals(viewName)) {
                        return new org.springframework.web.servlet.view.InternalResourceView("/templates/blog.html");
                    } else if ("article".equals(viewName)) {
                        return new org.springframework.web.servlet.view.InternalResourceView("/templates/article.html");
                    }
                    return null;
                })
                .build();
    }

    @Test
    void testBlogPage() throws Exception {
        // Arrange: mock the articleService to return a list of articles
        when(articleService.findAll()).thenReturn(List.of());  // Return an empty list for simplicity

        // Act & Assert: Send a GET request to "/blog" and verify the response
        mockMvc.perform(get("/blog"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"))  // Ensure "blog" matches the Thymeleaf template name
                .andExpect(model().attributeExists("articles"));
    }


    @Test
    void testArticlePage() throws Exception {
        // Arrange: mock the articleService to return an article by id
        Long articleId = 1L;
        when(articleService.findById(articleId)).thenReturn(new Article());  // Mock a sample article

        // Act & Assert: Send a GET request to "/blog/{id}" and verify the response
        mockMvc.perform(get("/blog/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(view().name("article"))  // Verify that the view is "article"
                .andExpect(model().attributeExists("article"));  // Verify that "article" attribute exists in the model
    }
}
