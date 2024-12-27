package ludo.mentis.aciem.auctoritas.repos;

import ludo.mentis.aciem.auctoritas.domain.Software;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SoftwareRepository extends JpaRepository<Software, Integer> {

    Page<Software> findAllById(Integer id, Pageable pageable);

}
