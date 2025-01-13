package ludo.mentis.aciem.auctoritas.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import ludo.mentis.aciem.auctoritas.domain.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
}
