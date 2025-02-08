package ludo.mentis.aciem.tabellarius.model;

import ludo.mentis.aciem.tabellarius.domain.BodyType;
import ludo.mentis.aciem.tabellarius.validation.UniqueFileName;
import ludo.mentis.aciem.tabellarius.validation.ValidEmailList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class MessageDTO {

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    @Size(max = 75000)
    private String body;

    @NotNull
    private BodyType bodyType;

    @ValidEmailList
    private List<String> recipientsTo;

    @ValidEmailList(allowNull = true, allowEmpty = true)
    private List<String> recipientsCc;

    @ValidEmailList(allowNull = true, allowEmpty = true)
    private List<String> recipientsBcc;

    @UniqueFileName
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