package ludo.mentis.aciem.tabellarius.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import ludo.mentis.aciem.tabellarius.domain.*;
import ludo.mentis.aciem.tabellarius.model.MessageAssembler;
import ludo.mentis.aciem.tabellarius.model.MessageDTO;
import ludo.mentis.aciem.tabellarius.repos.MessageRepository;
import ludo.mentis.aciem.tabellarius.util.LogHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final MessageRepository repository;
    private final SendMailService sendMailService;

    @Value("${app.email.from}")
    private String from;

    public EmailServiceImpl(MessageRepository repository, SendMailService sendMailService) {
        this.repository = repository;
        this.sendMailService = sendMailService;
    }

    @Override
    public boolean send(MessageDTO messageDTO, String senderIp) {
        LogHelper.traceMethodCall(EmailServiceImpl.class, "send", messageDTO, senderIp);

        var message = MessageAssembler.fromDTO(messageDTO);
        message.setFrom(from);

        logEmailDetails(message);

        try {
            sendMailService.execute(message);

            var messageLog = new MessageLog();
            messageLog.setMessage(message);
            messageLog.setSuccess(true);
            messageLog.setCreatedAt(LocalDateTime.now());
            messageLog.setSenderIp(senderIp);
            message.setLog(messageLog);

            return true;
        } catch (MessagingException e) {
            log.error("Error sending email", e);

            var messageError = new MessageError();
            messageError.setMessage(message);
            messageError.fromException(e);
            message.setError(messageError);

            return false;
        } finally {
            repository.save(message);
        }
    }

    private void logEmailDetails(Message message) {
        var recipients = String.join("; ",
                message.getRecipients()
                       .stream()
                       .filter(r -> r.getType() == RecipientType.TO)
                       .map(Recipient::getEmail)
                       .toList()
        );
        log.info("Sending email from {} to {} with subject '{}'", message.getFrom(), recipients, message.getSubject());
    }
}
