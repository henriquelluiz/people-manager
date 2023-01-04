package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddService {
    Address saveAddress(Long personId, Address entity);
    Address getAddressById(Long personId, Long addressId);
    void setPreferentialAddress(Long personId, Long addressId);
    Address getPreferentialAddress(Long personId);
    Page<Address> getAll(Long personId, Pageable pageable);
}
