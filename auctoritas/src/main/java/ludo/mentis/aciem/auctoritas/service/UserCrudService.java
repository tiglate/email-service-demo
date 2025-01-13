package ludo.mentis.aciem.auctoritas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ludo.mentis.aciem.auctoritas.model.UserDTO;

public interface UserCrudService {

    Page<UserDTO> findAll(String filter, Pageable pageable);

    UserDTO get(Integer id);

    Integer create(UserDTO userDTO);

    void update(Integer id, UserDTO userDTO);

    void delete(Integer id);
}
