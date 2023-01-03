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
        log.debug("");
        return personRepository.save(entity);
    }

    @Override
    public void updatePerson(Long id, Person entity) {
        log.debug("");
        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
        person.setFirstName(entity.getFirstName());
        person.setLastName(entity.getLastName());
        person.setDateOfBirth(entity.getDateOfBirth());
        log.debug("");
    }

    @Override
    public void deletePerson(Long id) {
        log.debug("");
        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
        personRepository.delete(person);
        log.debug("");
    }

    @Override
    public Person getPersonById(Long id) {
        log.debug("");
        return personRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
    }

    @Override
    public Page<Person> getAll(Pageable pageable) {
        log.debug("");
        return personRepository.findAll(pageable);
    }
}
