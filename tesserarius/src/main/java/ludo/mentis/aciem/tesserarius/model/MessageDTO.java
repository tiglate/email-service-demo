package ludo.mentis.aciem.tesserarius.model;

import java.util.List;

public class MessageDTO {

    private String subject;

    private String body;

    private BodyType bodyType;

    private List<String> recipientsTo;

    private List<String> recipientsCc;

    private List<String> recipientsBcc;

    private List<AttachmentDTO> attachments;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public List<String> getRecipientsTo() {
        return recipientsTo;
    }

    public void setRecipientsTo(List<String> recipientsTo) {
        this.recipientsTo = recipientsTo;
    }

    public List<String> getRecipientsCc() {
        return recipientsCc;
    }

    public void setRecipientsCc(List<String> recipientsCc) {
        this.recipientsCc = recipientsCc;
    }

    public List<String> getRecipientsBcc() {
        return recipientsBcc;
    }

    public void setRecipientsBcc(List<String> recipientsBcc) {
        this.recipientsBcc = recipientsBcc;
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }
}