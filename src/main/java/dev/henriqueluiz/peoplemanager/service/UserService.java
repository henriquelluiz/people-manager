package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Role;
import dev.henriqueluiz.peoplemanager.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    AppUser saveUser(AppUser entity);
    AppUser getUser(String email);
    void deleteUser(String email);
    Role saveRole(Role entity);
    void addRolesToUser(String role, String email);
    Page<Role> getRoles(Pageable pageable);
}
