package dev.henriqueluiz.peoplemanager.repository;

import dev.henriqueluiz.peoplemanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}