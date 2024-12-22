package ludo.mentis.aciem.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import ludo.mentis.aciem.demo.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class SendMailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private SendMailServiceImpl sendMailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute() throws MessagingException {
        // Arrange
        var message = new Message();
        message.setFrom("test@example.com");
        message.setSubject("Test Subject");
        message.setBody("Test Body");
        message.setBodyType(BodyType.TEXT);

        var recipient = new Recipient();
        recipient.setMessage(message);
        recipient.setEmail("recipient@example.com");
        recipient.setType(RecipientType.TO);
        message.getRecipients().add(recipient);

        var attachment = new Attachment();
        attachment.setMessage(message);
        attachment.setFileName("test.txt");
        attachment.setAttachment(new byte[]{1, 2, 3});
        message.getAttachments().add(attachment);

        var mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        var mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);

        // Act
        sendMailService.execute(message);

        // Assert
        verify(emailSender, times(1)).send(mimeMessageCaptor.capture());
    }
}