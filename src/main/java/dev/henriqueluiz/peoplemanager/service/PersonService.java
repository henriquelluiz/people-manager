package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {
    Person savePerson(Person entity);
    void updatePerson(Long id, Person entity);
    void deletePerson(Long id);
    Person getPersonById(Long id);
    Page<Person> getAll(Pageable pageable);
}
