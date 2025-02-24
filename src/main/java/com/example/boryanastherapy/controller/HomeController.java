package com.example.boryanastherapy.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/index"})
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("userRole", request.isUserInRole("ADMIN"));
        return "index";
    }
    @GetMapping("/confirmation")
    public String confirmation(){
        return "confirmation";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/terms")
    public String terms(){
        return "terms";
    }

    @GetMapping("/privacy")
    public String privacy(){
        return "privacy";
    }

    @GetMapping("/cookies")
    public String cookies(){
        return "cookies";
    }
}
