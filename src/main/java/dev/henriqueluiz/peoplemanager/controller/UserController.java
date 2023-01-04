package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.AppUser;
import dev.henriqueluiz.peoplemanager.model.Role;
import dev.henriqueluiz.peoplemanager.service.UserService;
import dev.henriqueluiz.peoplemanager.web.request.RoleRequest;
import dev.henriqueluiz.peoplemanager.web.request.RoleUserRequest;
import dev.henriqueluiz.peoplemanager.web.request.UserRequest;
import dev.henriqueluiz.peoplemanager.web.response.AbstractResponse;
import dev.henriqueluiz.peoplemanager.web.response.RoleResponse;
import dev.henriqueluiz.peoplemanager.web.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = { "/users/save" }, produces = { "application/hal+json" })
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserRequest req) {
        AppUser user = userService.saveUser(requestToEntity(req));
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .port(8080)
                        .path("/users/save")
                        .toUriString());
        UserResponse response = entityToResponse(user);
        response.add(
                linkTo(methodOn(UserController.class)
                        .getAllRoles(Pageable.ofSize(3)))
                        .withSelfRel()
        );
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping(value = { "/roles/save" }, produces = { "application/hal+json" })
    public ResponseEntity<RoleResponse> saveRole(@RequestBody @Valid RoleRequest req) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .port(8080)
                        .path("/roles/save")
                        .toUriString());
        // Saving new role
        userService.saveRole(new Role(null, req.roleName()));

        // HATEOAS
        RoleResponse response = new RoleResponse(req.roleName());
        response.add(
                linkTo(methodOn(UserController.class)
                        .addRolesToUser(new RoleUserRequest("roleName", "userEmail")))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(UserController.class)
                        .getAllRoles(Pageable.ofSize(3)))
                        .withSelfRel()
        );
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(value = { "/roles/get/all" }, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<RoleResponse>> getAllRoles(Pageable pageable) {
        List<RoleResponse> roles = userService.getRoles(pageable)
                .map(r -> new RoleResponse(r.getName()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(roles));
    }

    @GetMapping(value = { "/users/get" }, produces = { "application/hal+json" })
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        UserResponse response = entityToResponse(userService.getUser(email));
        response.add(
                linkTo(methodOn(UserController.class)
                        .addRolesToUser(new RoleUserRequest("roleName", "userEmail")))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(UserController.class)
                        .deleteUserByEmail(email))
                        .withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = { "/users/delete" }, produces = { "application/json" })
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = { "/roles/add" }, produces = { "application/hal+json" })
    public ResponseEntity<AbstractResponse> addRolesToUser(@RequestBody @Valid RoleUserRequest req) {
        var model = new AbstractResponse();
        userService.addRolesToUser(req.roleName(), req.userEmail());
        model.add(
                linkTo(methodOn(AuthenticationController.class)
                        .generateTokens(new UserRequest("example@mail.com", "example")))
                        .withRel("generateTokens")
        );
        return ResponseEntity.ok(model);
    }

    private AppUser requestToEntity(UserRequest req) {
        var entity = new AppUser();
        entity.setEmail(req.email());
        entity.setPassword(req.password());
        entity.setAuthorities(new ArrayList<>());
        return entity;
    }

    private UserResponse entityToResponse(AppUser entity) {
        return new UserResponse(entity.getEmail(), new ArrayList<>(entity.getAuthorities()));
    }
}
