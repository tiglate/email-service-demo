package ludo.mentis.aciem.auctoritas.dev;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsersLoader implements DataLoaderCommand {

    public static final int MAX_TO_CREATE = 30;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsersLoader(final UserRepository repository,
                       final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getName() {
        return "Users";
    }

    @Override
    public boolean canItRun() {
        return repository.count() <= 1;
    }

    @Override
    public int run() {
        var faker = new Faker();
        for (var i = 0; i < MAX_TO_CREATE; i++) {
            var user = new User();
            user.setUsername(faker.internet().username());
            user.setPassword(passwordEncoder.encode(faker.internet().password()));
            repository.save(user);
        }
        return MAX_TO_CREATE;
    }
}
