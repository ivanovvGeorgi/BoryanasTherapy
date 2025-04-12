package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.dto.MessageDTO;
import com.example.boryanastherapy.model.entity.Message;
import com.example.boryanastherapy.service.EmailService;
import com.example.boryanastherapy.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        contactController = new ContactController(emailService, messageService);

        // Configure Thymeleaf Template Resolver
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // Adjust prefix if needed
        templateResolver.setSuffix(".html");    // Assuming .html files
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false); // For testing, might want to disable cache

        // Configure Thymeleaf Spring Template Engine
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Configure Thymeleaf View Resolver
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");

        mockMvc = MockMvcBuilders.standaloneSetup(contactController)
                .setViewResolvers(viewResolver) // Add the view resolver
                .build();
    }


    @Test
    void testShowContactPage() throws Exception {
        // Perform GET request to /contact
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(view().name("contact")) // Expect the 'contact' view
                .andExpect(model().attributeExists("messageDTO")); // Expect messageDTO to be added to the model
    }

    @Test
    void testHandleContactFormWithValidData() throws Exception {
        // Prepare a valid MessageDTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setName("John Doe");
        messageDTO.setEmail("john.doe@example.com");
        messageDTO.setMessage("This is a test message.");

        // Mock the message service (make sure saveMessage does nothing)
        doNothing().when(messageService).saveMessage(any(Message.class));

        // Mock the email service (ensure it doesn't send real emails)
        doNothing().when(emailService).sendEmail(any(), any(), any());

        // Perform POST request to /send-message with valid data
        mockMvc.perform(post("/send-message")
                        .contentType("application/x-www-form-urlencoded")
                        .param("name", messageDTO.getName())
                        .param("email", messageDTO.getEmail())
                        .param("message", messageDTO.getMessage()))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attribute("successMessage", "Thank you! Your message has been sent."))
                .andExpect(model().attributeExists("messageDTO"));
    }



    @Test
    void testHandleContactFormWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/send-message")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Set the correct content type
                        .param("name", "") // Invalid data
                        .param("email", "invalid-email") // Invalid data
                        .param("subject", "Test Subject")
                        .param("message", "Test message"))
                .andExpect(status().isOk()) // Expect 200 because you're returning the form with errors
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasErrors("messageDTO"));
    }
}
