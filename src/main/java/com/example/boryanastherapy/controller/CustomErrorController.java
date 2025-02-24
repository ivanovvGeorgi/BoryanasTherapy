package com.example.boryanastherapy.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        model.addAttribute("path", path != null ? path.toString() : "unknown");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode", statusCode);

            return switch (statusCode) {
                case 404 -> {
                    model.addAttribute("errorMessage", "The page you are looking for does not exist.");
                    yield "error/404";
                }
                case 403 -> {
                    model.addAttribute("errorMessage", "You don't have permission to access this page.");
                    yield "error/403";
                }
                case 500 -> {
                    model.addAttribute("errorMessage", "Internal server error occurred.");
                    yield "error/500";
                }
                default -> {
                    model.addAttribute("errorMessage", message != null ? message.toString() : "An unexpected error occurred");
                    yield "error/error";
                }
            };
        }

        model.addAttribute("errorCode", "Unknown");
        model.addAttribute("errorMessage", "An unexpected error occurred");
        return "error/error";
    }
}