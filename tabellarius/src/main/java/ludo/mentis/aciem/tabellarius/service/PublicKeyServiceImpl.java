package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;
import ludo.mentis.aciem.tabellarius.util.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Map;

@Service
public class PublicKeyServiceImpl implements PublicKeyService {

    private final Environment environment;
    private final RestOperations restOperations;

    @Autowired
    public PublicKeyServiceImpl(Environment environment) {
        this.environment = environment;
        this.restOperations = new RestTemplate();
    }

    public PublicKeyServiceImpl(Environment environment, RestOperations restOperations) {
        this.environment = environment;
        this.restOperations = restOperations;
    }

    @Override
    public PublicKey getPublicKeyFromCertsEndpoint(String certsUrl) throws PublicKeyException {
        String publicKeyString;

        if (isTestProfileActive()) {
            publicKeyString = KeyUtils.getDummyPublicKey();
        } else {
            var response = restOperations.getForObject(certsUrl, Map.class);

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