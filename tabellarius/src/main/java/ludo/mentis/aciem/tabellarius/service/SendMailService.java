package ludo.mentis.aciem.tabellarius.service;

import jakarta.mail.MessagingException;
import ludo.mentis.aciem.tabellarius.domain.Message;

public interface SendMailService {

    void execute(Message message) throws MessagingException;
}
