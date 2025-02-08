package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.model.UserDTO;

public interface UserAssembler {
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    UserDTO mapToDTO(final User user, final UserDTO userDTO);

    User mapToEntity(UserDTO userDTO, User user);
}
