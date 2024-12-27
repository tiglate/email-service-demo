package ludo.mentis.aciem.tabellarius.repos;

import ludo.mentis.aciem.tabellarius.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Integer> {

}
