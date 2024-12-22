package ludo.mentis.aciem.demo.repos;

import ludo.mentis.aciem.demo.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Integer> {

}
