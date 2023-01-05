package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Address;
import dev.henriqueluiz.peoplemanager.model.Person;
import dev.henriqueluiz.peoplemanager.service.PersonService;
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
public class PersonController {
    private final PersonService personService;

    @PostMapping(value = { "/persons/save" }, produces = { "application/hal+json" })
    public ResponseEntity<Person> savePerson(@RequestBody @Valid Person req) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .port(8080)
                        .path("/persons/save")
                        .toUriString());
        // HATEOAS
        Person response = personService.savePerson(req);
        response.add(
                linkTo(methodOn(AddressController.class)
                        .saveAddress(req.getPersonId(), new Address()))
                        .withRel("createAddress")
        );
        response.add(
                linkTo(methodOn(PersonController.class)
                        .getPerson(req.getPersonId()))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(PersonController.class)
                        .getAll(Pageable.ofSize(3)))
                        .withSelfRel()
        );
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = { "/persons/update" }, produces = { "application/json" })
    public ResponseEntity<Void> updatePerson(@RequestParam Long personId, @RequestBody @Valid Person body) {
        personService.updatePerson(personId, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = { "/persons/delete" }, produces = { "application/json" })
    public ResponseEntity<Void> deletePerson(@RequestParam Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = { "/persons/get" }, produces = { "application/hal+json" })
    public ResponseEntity<Person> getPerson(@RequestParam Long personId) {
        Person response = personService.getPersonById(personId);
        response.add(
                linkTo(methodOn(PersonController.class)
                        .updatePerson(personId, new Person()))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(PersonController.class)
                        .deletePerson(personId))
                        .withSelfRel()
        );
        response.add(
                linkTo(methodOn(AddressController.class)
                        .getAll(personId, Pageable.ofSize(3)))
                        .withRel("getAddressesByPerson")
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = { "/persons/get/all" }, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<Person>> getAll(Pageable pageable) {
        return ResponseEntity.ok(CollectionModel.of(personService.getAll(pageable)));
    }
}
