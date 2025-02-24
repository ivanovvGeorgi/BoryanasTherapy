package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.dto.MessageDTO;
import com.example.boryanastherapy.model.entity.Message;
import com.example.boryanastherapy.model.mapper.MessageMapper;
import com.example.boryanastherapy.service.EmailService;
import com.example.boryanastherapy.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.boryanastherapy.model.enums.Constants.CONTACT_SUBJECT;
import static com.example.boryanastherapy.model.enums.Constants.OWNER_EMAIL;

@Controller
public class ContactController {

    private final EmailService emailService;
    private final MessageService messageService;

    @Autowired
    public ContactController(EmailService emailService, MessageService messageService) {
        this.emailService = emailService;
        this.messageService = messageService;
    }

    @GetMapping("/contact")
    public String showContactPage(Model model) {
        model.addAttribute("messageDTO", new MessageDTO()); // Add a new instance of MessageDTO
        return "contact";
    }

    @PostMapping("/send-message")
    public String handleContactForm(@Valid @ModelAttribute("messageDTO") MessageDTO messageDTO,
                                    BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "contact"; // Return to the form if there are errors
        }

        Message message = MessageMapper.toEntity(messageDTO); // Use the mapper!
        this.messageService.saveMessage(message);

        this.emailService.sendEmail(OWNER_EMAIL, CONTACT_SUBJECT, messageDTO.getMessage()); // Or message.getMessage()

        model.addAttribute("successMessage", "Thank you! Your message has been sent.");
        model.addAttribute("messageDTO", new MessageDTO()); //reset the form
        return "contact"; // Return to the contact page
    }
}