package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Role;
import dev.henriqueluiz.peoplemanager.model.AppUser;
import dev.henriqueluiz.peoplemanager.repository.RoleRepository;
import dev.henriqueluiz.peoplemanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        log.debug("");
        entity.setPassword(encoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    public AppUser getUser(String email) {
        log.debug("");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
    }

    @Override
    public void deleteUser(String email) {
        log.debug("");
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
        userRepository.delete(user);
        log.debug("");
    }

    @Override
    public Role saveRole(Role entity) {
        log.debug("");
        return roleRepository.save(entity);
    }

    @Override
    public void addRolesToUser(String roleName, String email) {
        log.debug("");
        if(roleName.equals("manager") || roleName.equals("admin")) {
            log.debug("");
            throw new RuntimeException();
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
        user.getAuthorities().add(role.getName());
        log.debug("");
    }

    @Override
    public Page<Role> getRoles(Pageable pageable) {
        log.debug("");
        return roleRepository.findAll(pageable);
    }
}
