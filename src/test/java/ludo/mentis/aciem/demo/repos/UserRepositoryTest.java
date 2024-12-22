package ludo.mentis.aciem.demo.repos;

import ludo.mentis.aciem.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setEmail("john.doe@example.com");

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void testReadUser() {
        User user = new User();
        user.setUsername("janedoe");
        user.setPassword("password123");
        user.setEmail("jane.doe@example.com");

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("janedoe");
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("johnsmith");
        user.setPassword("password123");
        user.setEmail("john.smith@example.com");

        User savedUser = userRepository.save(user);

        savedUser.setUsername("johnsmithupdated");
        User updatedUser = userRepository.save(savedUser);

        assertThat(updatedUser.getUsername()).isEqualTo("johnsmithupdated");
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("janesmith");
        user.setPassword("password123");
        user.setEmail("jane.smith@example.com");

        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        assertThat(deletedUser).isNotPresent();
    }

    @Test
    public void testFindByUsernameIgnoreCase() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setEmail("john.doe@example.com");

        userRepository.save(user);

        User foundUser = userRepository.findByUsernameIgnoreCase("JOHNDOE");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
    }

    @Test
    public void testFindAllById() {
        User user = new User();
        user.setUsername("janedoe");
        user.setPassword("password123");
        user.setEmail("jane.doe@example.com");

        userRepository.save(user);

        Page<User> usersPage = userRepository.findAllById(user.getId(), PageRequest.of(0, 10));

        assertThat(usersPage).isNotEmpty();
        assertThat(usersPage.getContent().get(0).getId()).isEqualTo(user.getId());
    }

    @Test
    public void testExistsByUsernameIgnoreCase() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("password123");
        user.setEmail("john.doe@example.com");

        userRepository.save(user);

        boolean exists = userRepository.existsByUsernameIgnoreCase("JOHNDOE");

        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByEmailIgnoreCase() {
        User user = new User();
        user.setUsername("janedoe");
        user.setPassword("password123");
        user.setEmail("jane.doe@example.com");

        userRepository.save(user);

        boolean exists = userRepository.existsByEmailIgnoreCase("JANE.DOE@EXAMPLE.COM");

        assertThat(exists).isTrue();
    }
}