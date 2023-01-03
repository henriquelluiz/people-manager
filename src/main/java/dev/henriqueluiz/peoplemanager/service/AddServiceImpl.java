package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Address;
import dev.henriqueluiz.peoplemanager.model.Person;
import dev.henriqueluiz.peoplemanager.repository.AddressRepository;
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
public class AddServiceImpl implements AddService {
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Override
    public Address saveAddress(Long personId, Address entity) {
        log.debug("");
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });

        boolean hasPreferential = addressRepository.findByPreferred(personId).isPresent();
        if(hasPreferential && entity.getPreferred().equals(true)) {
            log.debug("");
            throw new IllegalStateException();
        }
        entity.getPerson().add(person);
        return addressRepository.save(entity);
    }

    @Override
    public Address getAddressById(Long personId, Long addressId) {
        log.debug("");
        return addressRepository.findByPersonId(personId, addressId)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
    }

    @Override
    public void setPreferentialAddress(Long personId, Long addressId, Boolean value) {
        log.debug("");
        boolean hasPreferential = addressRepository.findByPreferred(personId).isPresent();
        if(hasPreferential) {
            log.debug("");
            throw new IllegalStateException();
        }

        Address address = addressRepository.findByPersonId(personId, addressId)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
        address.setPreferred(value);
        log.debug("");
    }

    @Override
    public Address getPreferentialAddress(Long personId) {
        log.debug("");
        return addressRepository.findByPreferred(personId)
                .orElseThrow(() -> {
                    log.debug("");
                    return new EntityNotFoundException();
                });
    }

    @Override
    public Page<Address> getAll(Long personId, Pageable pageable) {
        log.debug("");
        return addressRepository.findAllByPersonId(personId, pageable);
    }
}
