package ludo.mentis.aciem.auctoritas.service;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.exception.NotFoundException;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;

@Service
@Transactional
public class UserCrudServiceImpl implements UserCrudService {

    private final UserRepository userRepository;
    private final UserAssembler userAssembler;

    public UserCrudServiceImpl(final UserRepository userRepository,
                               final UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    @Override
    public Page<UserDTO> findAll(final String filter, final Pageable pageable) {
        Page<User> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = userRepository.findAllById(integerFilter, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(user -> userAssembler.mapToDTO(user, new UserDTO()))
                .collect(Collectors.toList()),
                pageable, page.getTotalElements());
    }

    @Override
    public UserDTO get(final Integer id) {
        return userRepository.findById(id)
                .map(user -> userAssembler.mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        userAssembler.mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    @Override
    public void update(final Integer id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userAssembler.mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    @Override
    public void delete(final Integer id) {
        userRepository.deleteById(id);
    }
}
