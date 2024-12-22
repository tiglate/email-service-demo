package ludo.mentis.aciem.demo.service;

import jakarta.mail.MessagingException;
import ludo.mentis.aciem.demo.domain.Message;

public interface SendMailService {

    void execute(Message message) throws MessagingException;
}
