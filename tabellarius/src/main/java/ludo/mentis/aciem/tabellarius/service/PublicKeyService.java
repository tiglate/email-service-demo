package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;

import java.security.PublicKey;

public interface PublicKeyService {
    PublicKey getPublicKeyFromCertsEndpoint(String certsUrl) throws PublicKeyException;
}
