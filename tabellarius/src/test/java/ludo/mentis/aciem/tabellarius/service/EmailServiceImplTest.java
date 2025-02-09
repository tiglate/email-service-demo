package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.domain.BodyType;
import ludo.mentis.aciem.tabellarius.domain.Message;
import ludo.mentis.aciem.tabellarius.model.AttachmentDTO;
import ludo.mentis.aciem.tabellarius.model.MessageDTO;
import ludo.mentis.aciem.tabellarius.repos.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
class EmailServiceImplTest {

    @Mock
    private MessageRepository repository;

    @Mock
    private SendMailService sendMailService;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendSuccess() {
        var messageDTO = createMessageDTO();
        var senderIp = "127.0.0.1";

        var result = emailService.send(messageDTO, senderIp);

        assertTrue(result);
        verify(repository).save(any(Message.class));
    }

    @Test
    void testSendFailure() throws MessagingException {
        var messageDTO = createMessageDTO();
        var senderIp = "127.0.0.1";

        doThrow(new MessagingException()).when(sendMailService).execute(any(Message.class));

        var result = emailService.send(messageDTO, senderIp);

        assertFalse(result);
        verify(repository).save(any(Message.class));
    }

    private MessageDTO createMessageDTO() {
        var messageDTO = new MessageDTO();
        messageDTO.setSubject("Test Subject");
        messageDTO.setBody("This is a test email body.");
        messageDTO.setBodyType(BodyType.TEXT);
        messageDTO.setRecipientsTo(List.of("recipient1@example.com", "recipient2@example.com"));
        messageDTO.setRecipientsCc(List.of("cc1@example.com"));
        messageDTO.setRecipientsBcc(List.of("bcc1@example.com"));
        messageDTO.setAttachments(List.of(new AttachmentDTO("test.txt", "text/plain", new byte[]{1, 2, 3})));
        return messageDTO;
    }
}
