package ludo.mentis.aciem.demo.service;

import ludo.mentis.aciem.demo.model.MessageDTO;

public interface EmailService {

    boolean send(MessageDTO messageDTO, String senderIp);
}
