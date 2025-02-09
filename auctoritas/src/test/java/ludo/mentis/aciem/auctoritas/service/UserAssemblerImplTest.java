package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.Role;
import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.exception.NotFoundException;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.repos.RoleRepository;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAssemblerImplTest {

    private UserAssemblerImpl userAssembler;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SoftwareRepository softwareRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userAssembler = new UserAssemblerImpl(passwordEncoder, roleRepository, softwareRepository);
    }

    @Test
    void testToDTO() {
        var user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRoles(Set.of(createValidRole()));
        user.setSoftware(createValidSoftware());

        var userDTO = userAssembler.toDTO(user);

        assertNotNull(userDTO);
        assertEquals(1, userDTO.getId());
        assertEquals("testUser", userDTO.getUsername());
        assertEquals("password", userDTO.getPassword());
        assertTrue(userDTO.isEnabled());
        assertEquals(1, userDTO.getRoles().get(0));
        assertEquals(1, userDTO.getSoftware());
    }

    @Test
    void testToEntity() {
        var userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("testUser");
        userDTO.setPassword("password");
        userDTO.setEnabled(true);
        userDTO.setRoles(List.of(1));
        userDTO.setSoftware(1);

        var role = createValidRole();
        var software = createValidSoftware();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findAllById(List.of(1))).thenReturn(List.of(role));
        when(softwareRepository.findById(1)).thenReturn(Optional.of(software));

        User user = userAssembler.toEntity(userDTO);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.isEnabled());
        assertEquals(1, user.getRoles().iterator().next().getId());
        assertEquals(1, user.getSoftware().getId());
    }

    @Test
    void testToEntity_RoleNotFound() {
        var userDTO = new UserDTO();
        userDTO.setRoles(List.of(1));

        when(roleRepository.findAllById(List.of(1))).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> userAssembler.toEntity(userDTO));
    }

    @Test
    void testToEntity_SoftwareNotFound() {
        var userDTO = new UserDTO();
        userDTO.setSoftware(1);

        when(roleRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(softwareRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userAssembler.toEntity(userDTO));
    }

    private Role createValidRole() {
        var role = new Role();
        role.setId(1);
        role.setCode("ROLE_USER");
        role.setDescription("User role");
        role.setDateCreated(OffsetDateTime.now());
        role.setLastUpdated(OffsetDateTime.now());
        return role;
    }

    private Software createValidSoftware() {
        var software = new Software();
        software.setId(1);
        software.setCode("code");
        software.setName("name");
        software.setDateCreated(OffsetDateTime.now());
        software.setLastUpdated(OffsetDateTime.now());
        return software;
    }
}