package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Address;
import dev.henriqueluiz.peoplemanager.service.AddService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddService addressService;

    @PostMapping(value = { "/addresses/save" }, produces = { "application/hal+json" })
    public ResponseEntity<Address> saveAddress(@RequestParam Long personId, @RequestBody @Valid Address req) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .port(8080)
                        .path("/addresses/save")
                        .toUriString());

        Address response = addressService.saveAddress(personId, req);
        response.add(
                linkTo(methodOn(AddressController.class)
                        .setPreferential(personId, req.getAddressId()))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(AddressController.class)
                        .getAll(personId, Pageable.ofSize(3)))
                        .withSelfRel()
        );
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(value = { "/addresses/get" }, produces = { "application/hal+json" })
    public ResponseEntity<Address> getAddressById(@RequestParam Long personId, @RequestParam Long addressId) {
        Address response = addressService.getAddressById(personId, addressId);
        response.add(
                linkTo(methodOn(AddressController.class)
                        .getPreferential(personId))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(AddressController.class)
                        .getAll(personId, Pageable.ofSize(3)))
                        .withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = { "/addresses/get/preferential" }, produces = { "application/hal+json" })
    public ResponseEntity<Address> getPreferential(@RequestParam Long personId) {
        Address response = addressService.getPreferentialAddress(personId);
        response.add(
                linkTo(methodOn(AddressController.class)
                        .setPreferential(personId, null))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(AddressController.class)
                        .getAll(personId, Pageable.ofSize(3)))
                        .withSelfRel()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = { "/addresses/edit/preferential" }, produces = { "application/json" })
    public ResponseEntity<Void> setPreferential(@RequestParam Long personId, @RequestParam Long addressId) {
        addressService.setPreferentialAddress(personId, addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = { "/addresses/get/all" }, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<Address>> getAll(@RequestParam Long personId, Pageable pageable) {
        return ResponseEntity.ok(CollectionModel.of(addressService.getAll(personId, pageable)));
    }
}
