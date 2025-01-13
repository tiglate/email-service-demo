package ludo.mentis.aciem.auctoritas.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.domain.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameIgnoreCase(String username);

    Page<User> findAllById(Integer id, Pageable pageable);

    User findFirstBySoftware(Software software);

    boolean existsByUsernameIgnoreCase(String username);

    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    String getUsernameById(final Integer id);
}
