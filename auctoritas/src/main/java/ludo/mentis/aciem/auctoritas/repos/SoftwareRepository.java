package ludo.mentis.aciem.auctoritas.repos;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SoftwareRepository extends JpaRepository<Software, Integer> {

	Page<Software> findAllById(Integer id, Pageable pageable);

    @Query("SELECT new ludo.mentis.aciem.auctoritas.model.SoftwareDTO(d.id, d.code, d.name, d.dateCreated, d.lastUpdated) " +
            "FROM Software d " +
            "WHERE (:code IS NULL OR d.code LIKE %:code%) " +
            "AND (:name IS NULL OR d.name LIKE %:name%)")
    Page<SoftwareDTO> findAllBySearchCriteria(
            @Param("code") String code,
            @Param("name") String name,
            Pageable pageable
    );
}
