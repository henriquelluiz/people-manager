package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Person;
import dev.henriqueluiz.peoplemanager.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Override
    public Person savePerson(Person entity) {
        log.debug("Saving a new person");
        return personRepository.save(entity);
    }

    @Override
    public void updatePerson(Long id, Person entity) {
        log.debug("Updating person with id: '{}'", id);
        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });
        person.setFirstName(entity.getFirstName());
        person.setLastName(entity.getLastName());
        person.setDateOfBirth(entity.getDateOfBirth());
        log.debug("Person successfully updated");
    }

    @Override
    public void deletePerson(Long id) {
        log.debug("Deleting person with id: '{}'", id);
        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });
        personRepository.delete(person);
        log.debug("Person successfully deleted");
    }

    @Override
    public Person getPersonById(Long id) {
        log.debug("Fetching person with id: '{}'", id);
        return personRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });
    }

    @Override
    public Page<Person> getAll(Pageable pageable) {
        log.debug("Fetching all persons");
        return personRepository.findAll(pageable);
    }
}
