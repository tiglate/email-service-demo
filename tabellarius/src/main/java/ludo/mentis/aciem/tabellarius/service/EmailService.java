package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.model.MessageDTO;

public interface EmailService {

    boolean send(MessageDTO messageDTO, String senderIp);
}
