package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SoftwareService {

    Page<SoftwareDTO> findAll(String filter, Pageable pageable);

    SoftwareDTO get(Integer id);

    Integer create(SoftwareDTO softwareDTO);

    void update(Integer id, SoftwareDTO softwareDTO);

    void delete(Integer id);

    ReferencedWarning getReferencedWarning(Integer id);

}
