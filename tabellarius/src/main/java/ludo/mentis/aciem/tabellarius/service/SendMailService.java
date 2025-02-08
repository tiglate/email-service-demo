package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.domain.Message;

import javax.mail.MessagingException;

public interface SendMailService {

    void execute(Message message) throws MessagingException;
}
