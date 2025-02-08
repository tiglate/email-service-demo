package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.commons.web.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SoftwareService {

    Page<SoftwareDTO> findAll(SoftwareDTO searchDTO, Pageable pageable);

    SoftwareDTO get(Integer id);

    Integer create(SoftwareDTO softwareDTO);

    void update(Integer id, SoftwareDTO softwareDTO);

    void delete(Integer id);

    ReferencedWarning getReferencedWarning(Integer id);

}
