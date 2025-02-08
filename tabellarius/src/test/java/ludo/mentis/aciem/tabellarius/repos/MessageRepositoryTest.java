package ludo.mentis.aciem.tabellarius.repos;

import ludo.mentis.aciem.tabellarius.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void testCreateMessage() {
        var message = createMessage();

        var savedMessage = messageRepository.save(message);

        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.getId()).isNotNull();
        assertThat(savedMessage.getLog()).isNotNull();
        assertThat(savedMessage.getRecipients()).isNotEmpty();
        assertThat(savedMessage.getAttachments()).isNotEmpty();

        var savedMessageLog = savedMessage.getLog();
        assertThat(savedMessageLog).isNotNull();
        assertThat(savedMessageLog.getId()).isNotNull();
        assertThat(savedMessageLog.getMessage().getId()).isEqualTo(savedMessage.getId());

        var savedRecipient = savedMessage.getRecipients().get(0);
        assertThat(savedRecipient).isNotNull();
        assertThat(savedRecipient.getId()).isNotNull();
        assertThat(savedRecipient.getMessage().getId()).isEqualTo(savedMessage.getId());

        var savedAttachment = savedMessage.getAttachments().get(0);
        assertThat(savedAttachment).isNotNull();
        assertThat(savedAttachment.getId()).isNotNull();
        assertThat(savedAttachment.getMessage().getId()).isEqualTo(savedMessage.getId());
    }

    @Test
    void testReadMessage() {
        var message = createMessage();
        var savedMessage = messageRepository.save(message);

        var foundMessage = messageRepository.findById(savedMessage.getId());

        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get().getId()).isEqualTo(savedMessage.getId());
        assertThat(foundMessage.get().getSubject()).isEqualTo("You won the lottery!");
        assertThat(foundMessage.get().getBody()).isEqualTo("Congratulations! You won the lottery! Please contact us to claim your prize.");
        assertThat(foundMessage.get().getFrom()).isEqualTo("noreply@fakelottery.com");

        var foundMessageLog = foundMessage.get().getLog();
        assertThat(foundMessageLog).isNotNull();
        assertThat(foundMessageLog.getId()).isNotNull();
        assertThat(foundMessageLog.getMessage().getId()).isEqualTo(savedMessage.getId());
        assertThat(foundMessageLog.getSuccess()).isTrue();
        assertThat(foundMessageLog.getSenderIp()).isEqualTo("192.168.1.1");

        var foundRecipient = foundMessage.get().getRecipients().get(0);
        assertThat(foundRecipient).isNotNull();
        assertThat(foundRecipient.getId()).isNotNull();
        assertThat(foundRecipient.getMessage().getId()).isEqualTo(savedMessage.getId());
        assertThat(foundRecipient.getType()).isEqualTo(RecipientType.TO);
        assertThat(foundRecipient.getEmail()).isEqualTo("winner@example.com");

        var foundAttachment = foundMessage.get().getAttachments().get(0);
        assertThat(foundAttachment).isNotNull();
        assertThat(foundAttachment.getId()).isNotNull();
        assertThat(foundAttachment.getMessage().getId()).isEqualTo(savedMessage.getId());
        assertThat(foundAttachment.getFileName()).isEqualTo("prize_details2.pdf");
        assertThat(foundAttachment.getFileType()).isEqualTo("application/pdf");
        assertThat(new String(foundAttachment.getData())).isEqualTo("test string");
    }

    @Test
    void testUpdateMessage() {
        var message = createMessage();
        var savedMessage = messageRepository.save(message);

        savedMessage.setSubject("Updated Subject");
        savedMessage.setBody("Updated Body");
        savedMessage.setFrom("updated@fakelottery.com");

        var updatedMessageLog = savedMessage.getLog();
        updatedMessageLog.setSuccess(false);
        updatedMessageLog.setSenderIp("192.168.1.2");

        var updatedRecipient = savedMessage.getRecipients().get(0);
        updatedRecipient.setType(RecipientType.CC);
        updatedRecipient.setEmail("updated@example.com");

        var updatedAttachment = savedMessage.getAttachments().get(0);
        updatedAttachment.setFileName("updated_prize_details.pdf");
        updatedAttachment.setFileType("application/updated-pdf");
        updatedAttachment.setData("updated test string".getBytes());

        var updatedMessage = messageRepository.save(savedMessage);

        assertThat(updatedMessage).isNotNull();
        assertThat(updatedMessage.getId()).isEqualTo(savedMessage.getId());
        assertThat(updatedMessage.getSubject()).isEqualTo("Updated Subject");
        assertThat(updatedMessage.getBody()).isEqualTo("Updated Body");
        assertThat(updatedMessage.getFrom()).isEqualTo("updated@fakelottery.com");

        var savedMessageLog = updatedMessage.getLog();
        assertThat(savedMessageLog).isNotNull();
        assertThat(savedMessageLog.getId()).isNotNull();
        assertThat(savedMessageLog.getMessage().getId()).isEqualTo(updatedMessage.getId());
        assertThat(savedMessageLog.getSuccess()).isFalse();
        assertThat(savedMessageLog.getSenderIp()).isEqualTo("192.168.1.2");

        var savedRecipient = updatedMessage.getRecipients().get(0);
        assertThat(savedRecipient).isNotNull();
        assertThat(savedRecipient.getId()).isNotNull();
        assertThat(savedRecipient.getMessage().getId()).isEqualTo(updatedMessage.getId());
        assertThat(savedRecipient.getType()).isEqualTo(RecipientType.CC);
        assertThat(savedRecipient.getEmail()).isEqualTo("updated@example.com");

        var savedAttachment = updatedMessage.getAttachments().get(0);
        assertThat(savedAttachment).isNotNull();
        assertThat(savedAttachment.getId()).isNotNull();
        assertThat(savedAttachment.getMessage().getId()).isEqualTo(updatedMessage.getId());
        assertThat(savedAttachment.getFileName()).isEqualTo("updated_prize_details.pdf");
        assertThat(savedAttachment.getFileType()).isEqualTo("application/updated-pdf");
        assertThat(new String(savedAttachment.getData())).isEqualTo("updated test string");
    }

    @Test
    void testDeleteMessage() {
        var message = createMessage();
        var savedMessage = messageRepository.save(message);

        messageRepository.deleteById(savedMessage.getId());

        var deletedMessage = messageRepository.findById(savedMessage.getId());

        assertThat(deletedMessage).isNotPresent();
    }

    private Message createMessage() {
        var message = new Message();
        message.setSubject("You won the lottery!");
        message.setBody("Congratulations! You won the lottery! Please contact us to claim your prize.");
        message.setBodyType(BodyType.TEXT);
        message.setFrom("noreply@fakelottery.com");

        var messageLog = new MessageLog();
        messageLog.setSuccess(true);
        messageLog.setSenderIp("192.168.1.1");
        messageLog.setMessage(message);
        message.setLog(messageLog);

        var recipient = new Recipient();
        recipient.setMessage(message);
        recipient.setType(RecipientType.TO);
        recipient.setEmail("winner@example.com");

        var attachment = new Attachment();
        attachment.setMessage(message);
        attachment.setFileName("prize_details2.pdf");
        attachment.setFileType("application/pdf");
        attachment.setData("test string".getBytes());

        message.getRecipients().add(recipient);
        message.getAttachments().add(attachment);

        return message;
    }
}