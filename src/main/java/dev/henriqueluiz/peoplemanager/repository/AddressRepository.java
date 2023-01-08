package dev.henriqueluiz.peoplemanager.repository;

import dev.henriqueluiz.peoplemanager.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(
            "SELECT a FROM Address a WHERE a.person.personId = ?1 " +
                    "AND a.addressId = ?2"
    )
    Optional<Address> findByPersonId(Long personId, Long addressId);

    @Query(
            "SELECT a FROM Address a WHERE a.person.personId = ?1 " +
                    "AND a.preferred = TRUE"
    )
    Optional<Address> findByPreferred(Long personId);

    @Query("SELECT a FROM Address a WHERE a.person.personId = ?1")
    Page<Address> findAllByPersonId(Long personId, Pageable pageable);
}
