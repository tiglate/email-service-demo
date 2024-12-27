package ludo.mentis.aciem.auctoritas.util;

public class UserRoles {

    public static final String ROLE_USER_WRITE = "ROLE_USER_WRITE";
    public static final String ROLE_USER_READ = "ROLE_USER_READ";
    public static final String ROLE_SOFTWARE_WRITE = "ROLE_SOFTWARE_WRITE";
    public static final String ROLE_SOFTWARE_READ = "ROLE_SOFTWARE_READ";

    private UserRoles() {
        throw new IllegalStateException("Utility class");
    }
}
