package ludo.mentis.aciem.auctoritas.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ludo.mentis.aciem.auctoritas.domain.Software;

public interface SoftwareRepository extends JpaRepository<Software, Integer> {

	Page<Software> findAllById(Integer id, Pageable pageable);

}
