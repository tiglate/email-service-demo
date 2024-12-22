package ludo.mentis.aciem.demo.repos;

import ludo.mentis.aciem.demo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameIgnoreCase(String username);

    Page<User> findAllById(Integer id, Pageable pageable);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

}
