package ludo.mentis.aciem.demo.model;

import ludo.mentis.aciem.demo.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageAssemblerTest {

    @Test
    void testFromDTO() {
        // Create a MessageDTO object and set its properties
        var messageDTO = new MessageDTO();
        messageDTO.setSubject("Test Subject");
        messageDTO.setBody("Test Body");
        messageDTO.setBodyType(BodyType.TEXT);

        // Add recipients
        messageDTO.setRecipientsTo(List.of("to@example.com"));
        messageDTO.setRecipientsCc(List.of("cc@example.com"));
        messageDTO.setRecipientsBcc(List.of("bcc@example.com"));

        // Add attachments
        var attachmentDTO = new AttachmentDTO("test.txt", "text/plain", new byte[]{1, 2, 3});
        attachmentDTO.setAttachment(new byte[]{1, 2, 3});
        messageDTO.setAttachments(List.of(attachmentDTO));

        // Convert to Message
        var message = MessageAssembler.fromDTO(messageDTO);

        // Assert the properties
        assertEquals(messageDTO.getSubject(), message.getSubject());
        assertEquals(messageDTO.getBody(), message.getBody());
        assertEquals(messageDTO.getBodyType(), message.getBodyType());

        // Assert recipients
        assertEquals(1, message.getRecipients().stream().filter(r -> r.getType() == RecipientType.TO).count());
        assertEquals(1, message.getRecipients().stream().filter(r -> r.getType() == RecipientType.CC).count());
        assertEquals(1, message.getRecipients().stream().filter(r -> r.getType() == RecipientType.BCC).count());

        // Assert attachments
        assertEquals(1, message.getAttachments().size());
        assertEquals(attachmentDTO.getFileName(), message.getAttachments().get(0).getFileName());
        assertEquals(attachmentDTO.getFileType(), message.getAttachments().get(0).getFileType());
        assertArrayEquals(attachmentDTO.getAttachment(), message.getAttachments().get(0).getAttachment());
    }

    @Test
    void testToDTO() {
        // Create a Message object and set its properties
        var message = new Message();
        message.setId(1);
        message.setFrom("test@example.com");
        message.setSubject("Test Subject");
        message.setBody("Test Body");
        message.setBodyType(BodyType.TEXT);

        // Add recipients
        var recipientTo = new Recipient();
        recipientTo.setEmail("to@example.com");
        recipientTo.setType(RecipientType.TO);
        recipientTo.setMessage(message);
        message.getRecipients().add(recipientTo);

        var recipientCc = new Recipient();
        recipientCc.setEmail("cc@example.com");
        recipientCc.setType(RecipientType.CC);
        recipientCc.setMessage(message);
        message.getRecipients().add(recipientCc);

        var recipientBcc = new Recipient();
        recipientBcc.setEmail("bcc@example.com");
        recipientBcc.setType(RecipientType.BCC);
        recipientBcc.setMessage(message);
        message.getRecipients().add(recipientBcc);

        // Add attachments
        var attachment = new Attachment();
        attachment.setFileName("test.txt");
        attachment.setFileType("text/plain");
        attachment.setAttachment(new byte[]{1, 2, 3});
        attachment.setMessage(message);
        message.getAttachments().add(attachment);

        // Convert to MessageDTO
        var messageDTO = MessageAssembler.toDTO(message);

        // Assert the properties
        assertEquals(message.getSubject(), messageDTO.getSubject());
        assertEquals(message.getBody(), messageDTO.getBody());
        assertEquals(message.getBodyType(), messageDTO.getBodyType());

        // Assert recipients
        assertEquals(1, messageDTO.getRecipientsTo().size());
        assertEquals("to@example.com", messageDTO.getRecipientsTo().get(0));
        assertEquals(1, messageDTO.getRecipientsCc().size());
        assertEquals("cc@example.com", messageDTO.getRecipientsCc().get(0));
        assertEquals(1, messageDTO.getRecipientsBcc().size());
        assertEquals("bcc@example.com", messageDTO.getRecipientsBcc().get(0));

        // Assert attachments
        assertEquals(1, messageDTO.getAttachments().size());
        assertEquals(attachment.getFileName(), messageDTO.getAttachments().get(0).getFileName());
        assertEquals(attachment.getFileType(), messageDTO.getAttachments().get(0).getFileType());
        assertArrayEquals(attachment.getAttachment(), messageDTO.getAttachments().get(0).getAttachment());
    }
}