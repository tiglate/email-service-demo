package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;
import ludo.mentis.aciem.tabellarius.util.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.PublicKey;
import java.util.Map;

@Service
public class PublicKeyServiceImpl implements PublicKeyService {

    private final Environment environment;
    private final RestTemplate restTemplate;

    @Value("${app.auth.service.url}")
    private String authServiceUrl;

    @Autowired
    public PublicKeyServiceImpl(Environment environment, RestTemplate restTemplate) {
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public PublicKey getPublicKey() throws PublicKeyException {
        return getPublicKey(URI.create(authServiceUrl).resolve("/oauth/certs").toString());
    }

    @Override
    public PublicKey getPublicKey(String certsUrl) throws PublicKeyException {
        String publicKeyString;

        if (isTestProfileActive()) {
            publicKeyString = KeyUtils.getDummyPublicKey();
        } else {
            var response = restTemplate.getForObject(certsUrl, Map.class);

            if (response == null) {
                throw new PublicKeyException("Failed to fetch public key");
            }

            publicKeyString = (String) response.get("public_key");

            if (publicKeyString == null || publicKeyString.isEmpty()) {
                throw new PublicKeyException("Failed to fetch public key");
            }
        }

        return KeyUtils.getPublicKey(publicKeyString);
    }

    private boolean isTestProfileActive() {
        return environment.getActiveProfiles().length > 0 && "test".equals(environment.getActiveProfiles()[0]);
    }
}