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
import org.springframework.web.bind.annotation.*;

import static com.example.boryanastherapy.model.enums.Constants.*;

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

    @PostMapping(value = "/send-message", produces = "text/html;charset=UTF-8", consumes = "application/x-www-form-urlencoded;charset=UTF-8")

    public String handleContactForm(@Valid @ModelAttribute("messageDTO") MessageDTO messageDTO,
                                    BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "contact"; // Return to the form if there are errors
        }
        //Show user email in message
        messageDTO.setMessage(String.format(MESSAGE_FORMAT, messageDTO.getEmail(), messageDTO.getMessage()));

        Message message = MessageMapper.toEntity(messageDTO); // Use the mapper!
        this.messageService.saveMessage(message);

        this.emailService.sendEmail(
                OWNER_EMAIL,
                String.format(CONTACT_SUBJECT, message.getName()),
                messageDTO.getMessage()); // Or message.getMessage()

        model.addAttribute("successMessage", "Thank you! Your message has been sent.");
        model.addAttribute("messageDTO", new MessageDTO()); //reset the form
        return "contact"; // Return to the contact page
    }
}