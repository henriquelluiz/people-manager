package dev.henriqueluiz.peoplemanager.repository;

import dev.henriqueluiz.peoplemanager.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}