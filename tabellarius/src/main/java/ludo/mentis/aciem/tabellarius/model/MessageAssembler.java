package ludo.mentis.aciem.tabellarius.model;

import ludo.mentis.aciem.tabellarius.domain.Attachment;
import ludo.mentis.aciem.tabellarius.domain.Message;
import ludo.mentis.aciem.tabellarius.domain.Recipient;
import ludo.mentis.aciem.tabellarius.domain.RecipientType;

import java.util.ArrayList;

public final class MessageAssembler {

    private MessageAssembler() {
        throw new IllegalStateException("Utility class");
    }

    public static Message fromDTO(MessageDTO messageDTO) {
        var message = new Message();

        message.setSubject(messageDTO.getSubject());
        message.setBody(messageDTO.getBody());
        message.setBodyType(messageDTO.getBodyType());

        if (messageDTO.getRecipientsTo() != null) {
            for (var email : messageDTO.getRecipientsTo()) {
                var recipient = new Recipient();
                recipient.setMessage(message);
                recipient.setEmail(email);
                recipient.setType(RecipientType.TO);
                message.getRecipients().add(recipient);
            }
        }

        if (messageDTO.getRecipientsCc() != null) {
            for (var email : messageDTO.getRecipientsCc()) {
                var recipient = new Recipient();
                recipient.setMessage(message);
                recipient.setEmail(email);
                recipient.setType(RecipientType.CC);
                message.getRecipients().add(recipient);
            }
        }

        if (messageDTO.getRecipientsBcc() != null) {
            for (var email : messageDTO.getRecipientsBcc()) {
                var recipient = new Recipient();
                recipient.setMessage(message);
                recipient.setEmail(email);
                recipient.setType(RecipientType.BCC);
                message.getRecipients().add(recipient);
            }
        }

        if (messageDTO.getAttachments() != null) {
            for (var attachmentDTO : messageDTO.getAttachments()) {
                var attachment = new Attachment();
                attachment.setMessage(message);
                attachment.setFileName(attachmentDTO.getFileName());
                attachment.setFileType(attachmentDTO.getFileType());
                attachment.setData(attachmentDTO.getAttachment());
                message.getAttachments().add(attachment);
            }
        }

        return message;
    }

    public static MessageDTO toDTO(Message message) {
        var messageDTO = new MessageDTO();

        messageDTO.setSubject(message.getSubject());
        messageDTO.setBody(message.getBody());
        messageDTO.setBodyType(message.getBodyType());

        messageDTO.setRecipientsTo(new ArrayList<>());
        messageDTO.setRecipientsCc(new ArrayList<>());
        messageDTO.setRecipientsBcc(new ArrayList<>());

        if (message.getRecipients() != null) {
            for (var recipient : message.getRecipients()) {
                switch (recipient.getType()) {
                    case TO:
                        messageDTO.getRecipientsTo().add(recipient.getEmail());
                        break;
                    case CC:
                        messageDTO.getRecipientsCc().add(recipient.getEmail());
                        break;
                    case BCC:
                        messageDTO.getRecipientsBcc().add(recipient.getEmail());
                        break;
                }
            }
        }

        messageDTO.setAttachments(new ArrayList<>());

        if (message.getAttachments() != null) {
            for (var attachment : message.getAttachments()) {
                var attachmentDTO = new AttachmentDTO(
                        attachment.getFileName(),
                        attachment.getFileType(),
                        attachment.getData()
                );
                messageDTO.getAttachments().add(attachmentDTO);
            }
        }

        return messageDTO;
    }
}
