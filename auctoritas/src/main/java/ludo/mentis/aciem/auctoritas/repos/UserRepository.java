package ludo.mentis.aciem.auctoritas.repos;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameIgnoreCase(String username);

    Page<User> findAllById(Integer id, Pageable pageable);

    User findFirstBySoftware(Software software);

    boolean existsByUsernameIgnoreCase(String username);

}
