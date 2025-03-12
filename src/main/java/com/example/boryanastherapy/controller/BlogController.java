package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private final ArticleService articleService;

    public BlogController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String blogPage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "blog"; // Name of your Thymeleaf template
    }
    @GetMapping("/{id}")
    public String articlePage(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article"; // Name of your single article template
    }
}