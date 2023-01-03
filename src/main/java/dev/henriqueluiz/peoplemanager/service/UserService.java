package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Role;
import dev.henriqueluiz.peoplemanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User saveUser(User entity);
    User getUser(String email);
    void deleteUser(String email);
    Role saveRole(Role entity);
    Role getRole(String name);
    void addRolesToUser(String role, String email);
    Page<Role> getRoles(Pageable pageable);
}
