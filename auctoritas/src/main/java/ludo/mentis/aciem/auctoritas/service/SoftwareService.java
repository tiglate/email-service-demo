package ludo.mentis.aciem.auctoritas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.util.ReferencedWarning;


public interface SoftwareService {

    Page<SoftwareDTO> findAll(SoftwareDTO searchDTO, Pageable pageable);

    SoftwareDTO get(Integer id);

    Integer create(SoftwareDTO softwareDTO);

    void update(Integer id, SoftwareDTO softwareDTO);

    void delete(Integer id);

    ReferencedWarning getReferencedWarning(Integer id);

}
