package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.entity.Article;
import com.example.boryanastherapy.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin/articles")
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "admin/articles/list";
    }

    @GetMapping("/create")
    public String createArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/articles/create";
    }

    @PostMapping("/save")
    public String saveArticle(@ModelAttribute Article article, RedirectAttributes redirectAttributes) {
        articleService.save(article);
        redirectAttributes.addFlashAttribute("success", "Article saved successfully");
        return "redirect:/admin/articles";
    }

    @GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "admin/articles/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        articleService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Article deleted successfully");
        return "redirect:/admin/articles";
    }
    @PostMapping("/upload-image")
    public @ResponseBody String uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        String uploadDir = "uploads/"; // Store files outside static resources
        if (!image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());
            return "/uploads/" + fileName; // Return the image URL
        }
        return "error";
    }

}