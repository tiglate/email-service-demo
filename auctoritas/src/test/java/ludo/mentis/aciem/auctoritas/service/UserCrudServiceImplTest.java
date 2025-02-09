package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.exception.NotFoundException;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCrudServiceImplTest {

    private UserCrudServiceImpl userCrudService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAssembler userAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCrudService = new UserCrudServiceImpl(userRepository, userAssembler);
    }

    @Test
    void testFindAll_WithFilter() {
        var pageable = PageRequest.of(0, 10);
        var user = new User();
        var page = new PageImpl<>(Collections.singletonList(user));
        var userDTO = new UserDTO();

        when(userRepository.findAllById(anyInt(), eq(pageable))).thenReturn(page);
        when(userAssembler.mapToDTO(any(User.class), any(UserDTO.class))).thenReturn(userDTO);

        var result = userCrudService.findAll("1", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userDTO, result.getContent().get(0));
    }

    @Test
    void testFindAll_WithoutFilter() {
        var pageable = PageRequest.of(0, 10);
        var user = new User();
        var page = new PageImpl<>(Collections.singletonList(user));
        var userDTO = new UserDTO();

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userAssembler.mapToDTO(any(User.class), any(UserDTO.class))).thenReturn(userDTO);

        var result = userCrudService.findAll(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userDTO, result.getContent().get(0));
    }

    @Test
    void testGet() {
        var user = new User();
        var userDTO = new UserDTO();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userAssembler.mapToDTO(any(User.class), any(UserDTO.class))).thenReturn(userDTO);

        var result = userCrudService.get(1);

        assertNotNull(result);
        assertEquals(userDTO, result);
    }

    @Test
    void testGet_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userCrudService.get(1));
    }

    @Test
    void testCreate() {
        var userDTO = new UserDTO();
        var user = new User();
        user.setId(1);

        when(userAssembler.mapToEntity(any(UserDTO.class), any(User.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        var result = userCrudService.create(userDTO);

        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    void testUpdate() {
        var userDTO = new UserDTO();
        var user = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userAssembler.mapToEntity(any(UserDTO.class), any(User.class))).thenReturn(user);

        userCrudService.update(1, userDTO);

        verify(userRepository).save(user);
    }

    @Test
    void testUpdate_NotFound() {
        var userDTO = new UserDTO();

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userCrudService.update(1, userDTO));
    }

    @Test
    void testDelete() {
        userCrudService.delete(1);

        verify(userRepository).deleteById(1);
    }
}