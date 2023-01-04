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

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddService addressService;

    @PostMapping(value = { "/addresses/save" }, produces = { "application/hal+json" })
    public ResponseEntity<Address> savePerson(@RequestParam Long personId, @RequestBody @Valid Address req) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .port(8080)
                        .path("/addresses/save")
                        .toUriString());
        return ResponseEntity.created(uri).body(addressService.saveAddress(personId, req));
    }

    @GetMapping(value = { "/addresses/get" }, produces = { "application/hal+json" })
    public ResponseEntity<Address> getAddressById(@RequestParam Long personId, @RequestParam Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(personId, addressId));
    }

    @GetMapping(value = { "/addresses/get/preferential" }, produces = { "application/hal+json" })
    public ResponseEntity<Address> getPreferential(@RequestParam("person_id") Long personId) {
        return ResponseEntity.ok(addressService.getPreferentialAddress(personId));
    }

    @PutMapping(value = { "/addresses/edit/preferential" }, produces = { "application/json" })
    public ResponseEntity<Void> setPreferential(@RequestParam Long personId, @RequestParam Long addressId, Boolean value) {
        addressService.setPreferentialAddress(personId, addressId, value);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = { "/addresses/get/all" }, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<Address>> getAll(@RequestParam Long personId, Pageable pageable) {
        return ResponseEntity.ok(CollectionModel.of(addressService.getAll(personId, pageable)));
    }
}
