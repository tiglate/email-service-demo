package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;
import ludo.mentis.aciem.auctoritas.util.NotFoundException;
import ludo.mentis.aciem.auctoritas.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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
    public Page<SoftwareDTO> findAll(final String filter, final Pageable pageable) {
        Page<Software> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = softwareRepository.findAllById(integerFilter, pageable);
        } else {
            page = softwareRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(application -> mapToDTO(application, new SoftwareDTO()))
                .toList(),
                pageable, page.getTotalElements());
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
