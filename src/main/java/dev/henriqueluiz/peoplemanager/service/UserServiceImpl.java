package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.exception.model.RoleNotAllowedException;
import dev.henriqueluiz.peoplemanager.exception.model.RoleNotFoundException;
import dev.henriqueluiz.peoplemanager.model.AppUser;
import dev.henriqueluiz.peoplemanager.model.Role;
import dev.henriqueluiz.peoplemanager.repository.RoleRepository;
import dev.henriqueluiz.peoplemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public AppUser saveUser(AppUser entity) {
        log.debug("Saving user: '{}'", entity.getEmail());
        entity.setPassword(encoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    public AppUser getUser(String email) {
        log.debug("Fetching user: '{}'", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("Username not found: '{}'", email);
                    return new UsernameNotFoundException(String.format("Username not found: '%s'", email));
                });
    }

    @Override
    public void deleteUser(String email) {
        log.debug("Deleting user: '{}'", email);
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("Username not found: '{}'", email);
                    return new UsernameNotFoundException(String.format("Username not found: '%s'", email));
                });
        userRepository.delete(user);
        log.debug("'{}' successfully deleted", email);
    }

    @Override
    public Role saveRole(Role entity) {
        log.debug("Saving role: '{}'", entity.getName());
        entity.setName(entity.getName().toLowerCase());
        return roleRepository.save(entity);
    }

    @Override
    public void addRolesToUser(String roleName, String email) {
        log.debug("Adding role '{}' to user: '{}'", roleName, email);

        if(roleName.equals("manager") || roleName.equals("admin")) {
            log.debug("Role not allowed: '{}'", roleName);
            throw new RoleNotAllowedException(String.format("Role not allowed: '%s'", roleName));
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    log.debug("Role not found: '{}'", roleName);
                    return new RoleNotFoundException(String.format("Role not found: '%s'", roleName));
                });

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("Username not found: '{}'", email);
                    return new UsernameNotFoundException(String.format("Username not found: '%s'", email));
                });

        user.getAuthorities().add(role.getName());
        log.debug("Role successfully added");
    }

    @Override
    public Page<Role> getRoles(Pageable pageable) {
        log.debug("Fetching all roles");
        return roleRepository.findAll(pageable);
    }
}
