package ludo.mentis.aciem.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ludo.mentis.aciem.demo.domain.BodyType;
import ludo.mentis.aciem.demo.model.AttachmentDTO;
import ludo.mentis.aciem.demo.model.MessageDTO;
import ludo.mentis.aciem.demo.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class EmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    void testSendEmailSuccess() throws Exception {
        var messageDTO = createValidMessageDTO();
        when(emailService.send(any(MessageDTO.class), any(String.class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(messageDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testSendEmailFailure() throws Exception {
        var messageDTO = createValidMessageDTO();
        when(emailService.send(any(MessageDTO.class), any(String.class))).thenReturn(false);

        mockMvc.perform(post("/api/v1/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(messageDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testSendEmailInvalid() throws Exception {
        var messageDTO = createInvalidMessageDTO();
        when(emailService.send(any(MessageDTO.class), any(String.class))).thenReturn(false);

        mockMvc.perform(post("/api/v1/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(messageDTO)))
                .andExpect(status().isBadRequest());
    }

    private MessageDTO createInvalidMessageDTO() {
        var messageDTO = new MessageDTO();
        messageDTO.setFrom("INVALID E-MAIL");
        messageDTO.setSubject(null);
        messageDTO.setBody("This is a test email body.");
        messageDTO.setBodyType(BodyType.TEXT);
        messageDTO.setRecipientsTo(List.of("recipient1@example.com", "recipient1@example.com"));
        messageDTO.setRecipientsCc(List.of("cc1@example.com"));
        messageDTO.setRecipientsBcc(List.of("bcc1@example.com"));
        messageDTO.setAttachments(List.of(new AttachmentDTO("test.txt", "text/plain", new byte[]{1, 2, 3})));
        return messageDTO;
    }

    private MessageDTO createValidMessageDTO() {
        var messageDTO = new MessageDTO();
        messageDTO.setFrom("sender@example.com");
        messageDTO.setSubject("Test Subject");
        messageDTO.setBody("This is a test email body.");
        messageDTO.setBodyType(BodyType.TEXT);
        messageDTO.setRecipientsTo(List.of("recipient1@example.com", "recipient2@example.com"));
        messageDTO.setRecipientsCc(List.of("cc1@example.com"));
        messageDTO.setRecipientsBcc(List.of("bcc1@example.com"));
        messageDTO.setAttachments(List.of(new AttachmentDTO("test.txt", "text/plain", new byte[]{1, 2, 3})));
        return messageDTO;
    }

    public String toJSON(MessageDTO dto) {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            return "{"
                    + "\"serialization_error\":\"" + e.getMessage() + "\""
                    + "}";
        }
    }
}