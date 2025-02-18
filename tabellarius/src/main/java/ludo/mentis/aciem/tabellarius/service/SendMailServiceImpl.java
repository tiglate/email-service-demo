package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.domain.BodyType;
import ludo.mentis.aciem.tabellarius.domain.Message;
import ludo.mentis.aciem.tabellarius.util.LogHelper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class SendMailServiceImpl implements SendMailService {

    private final JavaMailSender emailSender;

    public SendMailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void execute(Message message) throws MessagingException {
        LogHelper.traceMethodCall(SendMailServiceImpl.class, "execute", message);

        var mimeMessage = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(message.getFrom());

        for (var recipient : message.getRecipients()) {
            switch (recipient.getType()) {
                case TO:
                    helper.addTo(recipient.getEmail());
                    break;
                case CC:
                    helper.addCc(recipient.getEmail());
                    break;
                case BCC:
                    helper.addBcc(recipient.getEmail());
                    break;
            }
        }

        helper.setSubject(message.getSubject());
        helper.setText(message.getBody(), message.getBodyType() == BodyType.HTML);

        for (var attachment : message.getAttachments()) {
            helper.addAttachment(attachment.getFileName(), new ByteArrayResource(attachment.getData()));
        }

        emailSender.send(mimeMessage);
    }
}
