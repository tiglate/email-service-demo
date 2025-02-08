package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.Role;
import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.exception.NotFoundException;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.repos.RoleRepository;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class UserAssemblerImpl implements UserAssembler {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SoftwareRepository softwareRepository;

    public UserAssemblerImpl(final PasswordEncoder passwordEncoder,
                             final RoleRepository roleRepository,
                             final SoftwareRepository softwareRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.softwareRepository = softwareRepository;
    }

    @Override
    public UserDTO toDTO(User user) {
        var userDTO = new UserDTO();
        return mapToDTO(user, userDTO);
    }

    @Override
    public UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setAccountExpirationDate(user.getAccountExpirationDate());
        userDTO.setFailedLoginAttempts(user.getFailedLoginAttempts());
        userDTO.setLastFailedLoginAttempt(user.getLastFailedLoginAttempt());
        userDTO.setAccountLocked(user.isAccountLocked());
        userDTO.setSoftware(user.getSoftware() == null ? null : user.getSoftware().getId());
        userDTO.setRoles(user.getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toList()));
        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        final var user = new User();
        user.setId(userDTO.getId());
        return mapToEntity(userDTO, user);
    }

    @Override
    public User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(userDTO.isEnabled());
        user.setAccountExpirationDate(userDTO.getAccountExpirationDate());

        final var roles = roleRepository.findAllById(userDTO.getRoles() == null ? Collections.emptyList() : userDTO.getRoles());
        if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
            throw new NotFoundException("one of roles not found");
        }
        user.setRoles(new HashSet<>(roles));

        final var software = userDTO.getSoftware() == null ? null : softwareRepository.findById(userDTO.getSoftware())
                .orElseThrow(() -> new NotFoundException("software not found"));
        user.setSoftware(software);

        return user;
    }
}
