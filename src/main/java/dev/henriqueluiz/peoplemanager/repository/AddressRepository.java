package dev.henriqueluiz.peoplemanager.repository;

import dev.henriqueluiz.peoplemanager.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(
            "SELECT a FROM Address a JOIN Person p " +
                    "ON p.id = ?1 AND a.id = ?2"
    )
    Optional<Address> findByPersonId(Long personId, Long addressId);

    @Query(
            "SELECT a FROM Address a JOIN Person p " +
                    "ON p.id = ?1 AND a.preferred IS TRUE"
    )
    Optional<Address> findByPreferred(Long id);

    @Query("SELECT a FROM Address WHERE a.person.id = ?1")
    Page<Address> findAllByPersonId(Long id, Pageable pageable);
}