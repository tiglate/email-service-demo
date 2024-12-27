package ludo.mentis.aciem.auctoritas.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.model.FormSecurityConfigUserDetails;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public FormUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FormSecurityConfigUserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        var authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.getRole())).toList();

        return new FormSecurityConfigUserDetails(user.getId(), username, user.getPassword(), authorities);
    }

}
