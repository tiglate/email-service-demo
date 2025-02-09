package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.exception.NotFoundException;
import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;
import ludo.mentis.aciem.commons.web.ReferencedWarning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SoftwareServiceImplTest {

    private SoftwareServiceImpl softwareService;

    @Mock
    private SoftwareRepository softwareRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        softwareService = new SoftwareServiceImpl(softwareRepository, userRepository);
    }

    @Test
    void testFindAll() {
        SoftwareDTO searchDTO = new SoftwareDTO();
        searchDTO.setCode("code");
        searchDTO.setName("name");
        Pageable pageable = PageRequest.of(0, 10);
        Page<SoftwareDTO> expectedPage = new PageImpl<>(List.of(new SoftwareDTO()));

        when(softwareRepository.findAllBySearchCriteria("code", "name", pageable)).thenReturn(expectedPage);

        Page<SoftwareDTO> result = softwareService.findAll(searchDTO, pageable);

        assertEquals(expectedPage, result);
    }

    @Test
    void testGet() {
        Software software = new Software();
        software.setId(1);
        software.setCode("code");
        software.setName("name");

        when(softwareRepository.findById(1)).thenReturn(Optional.of(software));

        SoftwareDTO result = softwareService.get(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("code", result.getCode());
        assertEquals("name", result.getName());
    }

    @Test
    void testGet_NotFound() {
        when(softwareRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> softwareService.get(1));
    }

    @Test
    void testCreate() {
        SoftwareDTO softwareDTO = new SoftwareDTO();
        softwareDTO.setCode("code");
        softwareDTO.setName("name");

        Software software = new Software();
        software.setId(1);

        when(softwareRepository.save(any(Software.class))).thenReturn(software);

        Integer result = softwareService.create(softwareDTO);

        assertEquals(1, result);
    }

    @Test
    void testUpdate() {
        SoftwareDTO softwareDTO = new SoftwareDTO();
        softwareDTO.setCode("code");
        softwareDTO.setName("name");

        Software software = new Software();
        software.setId(1);

        when(softwareRepository.findById(1)).thenReturn(Optional.of(software));

        softwareService.update(1, softwareDTO);

        verify(softwareRepository).save(software);
    }

    @Test
    void testUpdate_NotFound() {
        SoftwareDTO softwareDTO = new SoftwareDTO();

        when(softwareRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> softwareService.update(1, softwareDTO));
    }

    @Test
    void testDelete() {
        softwareService.delete(1);

        verify(softwareRepository).deleteById(1);
    }

    @Test
    void testGetReferencedWarning() {
        Software software = new Software();
        software.setId(1);

        User user = new User();
        user.setId(1);

        when(softwareRepository.findById(1)).thenReturn(Optional.of(software));
        when(userRepository.findFirstBySoftware(software)).thenReturn(user);

        ReferencedWarning result = softwareService.getReferencedWarning(1);

        assertNotNull(result);
        assertEquals("software.user.software.referenced", result.getKey());
        assertEquals(1, result.getParams().get(0));
    }

    @Test
    void testGetReferencedWarning_NoReference() {
        Software software = new Software();
        software.setId(1);

        when(softwareRepository.findById(1)).thenReturn(Optional.of(software));
        when(userRepository.findFirstBySoftware(software)).thenReturn(null);

        ReferencedWarning result = softwareService.getReferencedWarning(1);

        assertNull(result);
    }
}