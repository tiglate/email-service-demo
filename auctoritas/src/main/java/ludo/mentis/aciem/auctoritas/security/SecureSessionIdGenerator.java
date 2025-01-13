package ludo.mentis.aciem.auctoritas.security;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import org.apache.catalina.util.SessionIdGeneratorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * SessionIdGeneratorBase implementation using NativePRNGNonBlocking or Windows-PRNG for secure random generation.
 */
@Component
@Primary
public class SecureSessionIdGenerator extends SessionIdGeneratorBase {

    private static final int DEFAULT_SESSION_ID_LENGTH = 16; // Length in bytes
    private final SecureRandom secureRandom;
    private final int sessionIdLength;

    /**
     * Constructor with default session ID length (16 bytes).
     */
    @Autowired
    public SecureSessionIdGenerator() {
        this(DEFAULT_SESSION_ID_LENGTH);
    }

    /**
     * Constructor with custom session ID length.
     *
     * @param sessionIdLength Length of the session ID in bytes.
     */
    public SecureSessionIdGenerator(int sessionIdLength) {
        this.sessionIdLength = sessionIdLength;
        this.secureRandom = createSecureRandomInstance();
    }

    /**
     * Generate a new session ID.
     *
     * @return A securely generated session ID as a Base64-encoded string.
     */
    @Override
    public String generateSessionId() {
        final byte[] randomBytes = new byte[sessionIdLength];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Generate a new session ID for a specific route.
     *
     * @param route The route for which the session ID is being generated.
     * @return A securely generated session ID with the route included.
     */
    @Override
    public String generateSessionId(String route) {
        if (route == null || route.isEmpty()) {
            throw new IllegalArgumentException("Route cannot be null or empty");
        }
        final byte[] randomBytes = new byte[sessionIdLength];
        secureRandom.nextBytes(randomBytes);
        final String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return route + "-" + randomPart;
    }

    /**
     * Create a SecureRandom instance using NativePRNGNonBlocking or WINDOWS-PRNG.
     *
     * @return A SecureRandom instance.
     */
    private SecureRandom createSecureRandomInstance() {
        try {
        	String algorithm = "";
        	final var algorithmNames = Security.getAlgorithms("SecureRandom");
        	if (algorithmNames.contains("NativePRNGNonBlocking")) {
        		algorithm = "NativePRNGNonBlocking";
        	} else if (algorithmNames.contains("WINDOWS-PRNG")) {
        		algorithm = "WINDOWS-PRNG";
        	}
            return SecureRandom.getInstance(algorithm);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize SecureRandom with NativePRNGNonBlocking or Windows-PRNG", e);
        }
    }
}

