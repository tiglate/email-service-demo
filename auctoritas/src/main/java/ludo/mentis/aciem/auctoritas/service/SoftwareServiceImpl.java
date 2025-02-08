package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.commons.web.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.exception.NotFoundException;
import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;


@Service
public class SoftwareServiceImpl implements SoftwareService {

    private final SoftwareRepository softwareRepository;
    private final UserRepository userRepository;

    public SoftwareServiceImpl(final SoftwareRepository softwareRepository,
                               final UserRepository userRepository) {
        this.softwareRepository = softwareRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<SoftwareDTO> findAll(SoftwareDTO searchDTO, Pageable pageable) {
        return softwareRepository.findAllBySearchCriteria(
                searchDTO.getCode(),
                searchDTO.getName(),
                pageable
        );
    }

    @Override
    public SoftwareDTO get(final Integer id) {
        return softwareRepository.findById(id)
                .map(application -> mapToDTO(application, new SoftwareDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Integer create(final SoftwareDTO softwareDTO) {
        final Software software = new Software();
        mapToEntity(softwareDTO, software);
        return softwareRepository.save(software).getId();
    }

    @Override
    public void update(final Integer id, final SoftwareDTO softwareDTO) {
        final Software software = softwareRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(softwareDTO, software);
        softwareRepository.save(software);
    }

    @Override
    public void delete(final Integer id) {
        softwareRepository.deleteById(id);
    }

    private SoftwareDTO mapToDTO(final Software software,
                                 final SoftwareDTO softwareDTO) {
        softwareDTO.setId(software.getId());
        softwareDTO.setCode(software.getCode());
        softwareDTO.setName(software.getName());
        softwareDTO.setLastUpdated(software.getLastUpdated());
        softwareDTO.setDateCreated(software.getDateCreated());
        return softwareDTO;
    }

    private Software mapToEntity(final SoftwareDTO softwareDTO,
                                 final Software software) {
        software.setCode(softwareDTO.getCode());
        software.setName(softwareDTO.getName());
        return software;
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Software software = softwareRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User applicationUser = userRepository.findFirstBySoftware(software);
        if (applicationUser != null) {
            referencedWarning.setKey("software.user.software.referenced");
            referencedWarning.addParam(applicationUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
