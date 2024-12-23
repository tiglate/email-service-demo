package ludo.mentis.aciem.demo.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import ludo.mentis.aciem.demo.domain.MessageError;
import ludo.mentis.aciem.demo.domain.MessageLog;
import ludo.mentis.aciem.demo.model.MessageAssembler;
import ludo.mentis.aciem.demo.model.MessageDTO;
import ludo.mentis.aciem.demo.repos.MessageRepository;
import ludo.mentis.aciem.demo.util.LogHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final MessageRepository repository;
    private final SendMailService sendMailService;

    public EmailServiceImpl(MessageRepository repository, SendMailService sendMailService) {
        this.repository = repository;
        this.sendMailService = sendMailService;
    }

    @Override
    public boolean send(MessageDTO messageDTO, String senderIp) {
        LogHelper.traceMethodCall(EmailServiceImpl.class, "send", messageDTO, senderIp);
        logEmailDetails(messageDTO);

        var message = MessageAssembler.fromDTO(messageDTO);

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

    private void logEmailDetails(MessageDTO messageDTO) {
        log.info("Sending email from {} to {} with subject '{}'", messageDTO.getFrom(), String.join("; ", messageDTO.getRecipientsTo()), messageDTO.getSubject());
    }
}
