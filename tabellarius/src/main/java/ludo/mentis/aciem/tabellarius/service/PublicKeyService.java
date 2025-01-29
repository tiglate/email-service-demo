package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;

import java.security.PublicKey;

public interface PublicKeyService {

    PublicKey getPublicKey() throws PublicKeyException;

    PublicKey getPublicKey(String certsUrl) throws PublicKeyException;
}
