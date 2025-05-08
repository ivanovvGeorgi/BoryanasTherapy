package com.example.boryanastherapy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/services")
public class ServicesController {
    @GetMapping()
    public String services() {
        return "services"; // No leading slash
    }
    @GetMapping("/counselling")
    public String individual() {
        return "counselling"; // No leading slash
    }
    @GetMapping("/vret")
    public String vret() {
        return "vret"; // No leading slash
    }
}