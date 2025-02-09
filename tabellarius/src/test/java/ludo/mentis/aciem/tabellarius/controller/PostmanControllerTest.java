package ludo.mentis.aciem.tabellarius.controller;

import ludo.mentis.aciem.tabellarius.model.MessageDTO;
import ludo.mentis.aciem.tabellarius.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostmanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PostmanController postmanController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postmanController).build();
    }

    @Test
    void testShowEmailForm() throws Exception {
        mockMvc.perform(get("/postman"))
                .andExpect(status().isOk())
                .andExpect(view().name("postman/emailForm"))
                .andExpect(model().attributeExists("messageDTO"));
    }

    @Test
    void testSendEmailSuccess() throws Exception {
        when(emailService.send(any(MessageDTO.class), any(String.class))).thenReturn(true);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test content".getBytes());
        mockMvc.perform(multipart("/postman")
                        .file(file)
                        .param("subject", "Test Subject")
                        .param("body", "This is a test email body.")
                        .param("bodyType", "TEXT")
                        .param("recipientsTo", "recipient1@example.com")
                        .param("recipientsTo", "recipient2@example.com")
                        .param("recipientsCc", "cc1@example.com")
                        .param("recipientsBcc", "bcc1@example.com")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(view().name("postman/emailResult"))
                .andExpect(model().attribute("message", "Email sent successfully"));
    }

    @Test
    void testSendEmailFailure() throws Exception {
        when(emailService.send(any(MessageDTO.class), any(String.class))).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "test content".getBytes());
        mockMvc.perform(multipart("/postman")
                        .file(file)
                        .param("subject", "Test Subject")
                        .param("body", "This is a test email body.")
                        .param("bodyType", "TEXT")
                        .param("recipientsTo", "recipient1@example.com")
                        .param("recipientsTo", "recipient2@example.com")
                        .param("recipientsCc", "cc1@example.com")
                        .param("recipientsBcc", "bcc1@example.com")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(view().name("postman/emailResult"))
                .andExpect(model().attribute("message", "Failed to send email"));
    }

    @Test
    void testSendEmailValidationFailure() throws Exception {
        mockMvc.perform(multipart("/postman")
                        .param("subject", "")
                        .param("body", "This is a test email body.")
                        .param("bodyType", "TEXT")
                        .param("recipientsTo", "recipient1@example.com")
                        .param("recipientsTo", "recipient2@example.com")
                        .param("recipientsCc", "cc1@example.com")
                        .param("recipientsBcc", "bcc1@example.com")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(view().name("postman/emailForm"));
    }
}