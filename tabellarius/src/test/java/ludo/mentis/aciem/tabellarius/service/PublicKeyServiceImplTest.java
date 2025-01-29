package ludo.mentis.aciem.tabellarius.service;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;
import ludo.mentis.aciem.tabellarius.util.KeyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicKeyServiceImplTest {

    private static final String VALID_URL = "http://localhost:9000/oauth/certs";
    private static final String VALID_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlJ3D1bOXDD/FhHjVPWHULVFmlo/pVvp7s"
                    + "FkXHnF8CLWZqDx5ZcQLcVrcAnHApMsiuEzHnW1FZSPNP0ERt3vqMtHhbZrQXrt4+M0CsHqYGdzYB"
                    + "KuqUSPuIMhSxmAubwVVuxyoF+OoQ7M8sep01Je82sy4X7avW6LHFXBXNlOsVqyARGQ2DlmbpquV+"
                    + "wJ88m0k8m7/WY/IDxXYQbpXuUZG15YuU3T2F8A8y8KVsWBkBpgRFsmb+6CxZY0UivjjBaUKb1D/f"
                    + "tbEi0Z4JAH1v8EB6iAcwzNvNKBl1XQLUklw5YLwotGvazBaD2Av/g+C52zHJwesz1pE0CP7C+HR8"
                    + "6yyNwIDAQAB";

    @Mock
    private Environment environment;

    @Mock
    private RestTemplate restTemplate;

    private PublicKeyServiceImpl publicKeyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publicKeyService = new PublicKeyServiceImpl(environment, restTemplate);
    }

    @Test
    void testGetPublicKey_withTestProfile() throws Exception {
        when(environment.getActiveProfiles()).thenReturn(new String[]{"test"});
        String dummyPublicKey = KeyUtils.getDummyPublicKey();
        PublicKey publicKey = KeyUtils.getPublicKey(dummyPublicKey);

        PublicKey result = publicKeyService.getPublicKey("dummyUrl");

        assertEquals(publicKey, result);
    }

    @Test
    void testGetPublicKey_withValidResponse() throws Exception {
        when(environment.getActiveProfiles()).thenReturn(new String[]{});
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("public_key", VALID_PUBLIC_KEY));

        var publicKey = KeyUtils.getPublicKey(VALID_PUBLIC_KEY);

        var result = publicKeyService.getPublicKey(VALID_URL);

        assertEquals(publicKey, result);
    }

    @Test
    void testGetPublicKey_withNullResponse() {
        when(environment.getActiveProfiles()).thenReturn(new String[]{});
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        assertThrows(PublicKeyException.class, () -> publicKeyService.getPublicKey("invalidUrl"));
    }

    @Test
    void testGetPublicKeyFromCertsEndpoint_withEmptyPublicKey() {
        when(environment.getActiveProfiles()).thenReturn(new String[]{});
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("public_key", ""));

        assertThrows(PublicKeyException.class, () -> publicKeyService.getPublicKey("invalidUrl"));
    }
}