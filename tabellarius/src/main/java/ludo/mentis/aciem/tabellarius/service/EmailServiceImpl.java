package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.domain.*;
import ludo.mentis.aciem.tabellarius.model.MessageAssembler;
import ludo.mentis.aciem.tabellarius.model.MessageDTO;
import ludo.mentis.aciem.tabellarius.repos.MessageRepository;
import ludo.mentis.aciem.tabellarius.util.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private final MessageRepository repository;
    private final SendMailService sendMailService;

    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

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
        var recipients = message.getRecipients()
               .stream()
               .filter(r -> r.getType() == RecipientType.TO)
               .map(Recipient::getEmail)
               .collect(Collectors.joining("; "));
        log.info("Sending email from {} to {} with subject '{}'", message.getFrom(), recipients, message.getSubject());
    }
}
