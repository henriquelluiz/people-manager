package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

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
        return ResponseEntity.created(uri).body(personService.savePerson(req));
    }

    @PutMapping(value = { "/persons/update" }, produces = { "application/json" })
    public ResponseEntity<Void> updatePerson(@RequestParam Long id, @RequestBody @Valid Person body) {
        personService.updatePerson(id, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = { "/persons/delete" }, produces = { "application/json" })
    public ResponseEntity<Void> deletePerson(@RequestParam Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = { "/persons/get" }, produces = { "application/hal+json" })
    public ResponseEntity<Person> getPerson(@RequestParam Long id) {
        Person response = personService.getPersonById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = { "/persons/get/all" }, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<Person>> getAll(Pageable pageable) {
        return ResponseEntity.ok(CollectionModel.of(personService.getAll(pageable)));
    }
}
