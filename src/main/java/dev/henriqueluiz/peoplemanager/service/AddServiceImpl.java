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

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AddServiceImpl implements AddService {
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    @Override
    public Address saveAddress(Long personId, Address entity) {
        log.debug("Saving new address for person with id: '{}'", personId);
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });

        if(entity.getPreferred().equals(true)) {
            Optional<Address> preferred = addressRepository.findByPreferred(personId);
            preferred.ifPresent(address -> address.setPreferred(false));
        }
        entity.setPerson(person);
        return addressRepository.save(entity);
    }

    @Override
    public Address getAddressById(Long personId, Long addressId) {
        log.debug("Fetching address with id: '{}'", addressId);
        return addressRepository.findByPersonId(personId, addressId)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });
    }

    @Override
    public void setPreferentialAddress(Long personId, Long addressId) {
        log.debug("Setting preferred address for person with id: '{}'", personId);
        Optional<Address> preferred = addressRepository.findByPreferred(personId);
        preferred.ifPresent(address -> address.setPreferred(false));
        Address address = addressRepository.findByPersonId(personId, addressId)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });
        if (!address.getPreferred()) { address.setPreferred(true); }
        log.debug("Preferred address set successfully");
    }

    @Override
    public Address getPreferentialAddress(Long personId) {
        log.debug("Fetching preferred address for person with id: '{}'", personId);
        return addressRepository.findByPreferred(personId)
                .orElseThrow(() -> {
                    log.debug("Entity not found");
                    return new EntityNotFoundException("Entity not found");
                });
    }

    @Override
    public Page<Address> getAll(Long personId, Pageable pageable) {
        log.debug("Fetching all addresses for person with id: '{}'", personId);
        return addressRepository.findAllByPersonId(personId, pageable);
    }
}
