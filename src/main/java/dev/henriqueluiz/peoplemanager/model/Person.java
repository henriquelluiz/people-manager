package dev.henriqueluiz.peoplemanager.model;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@NoArgsConstructor
@Relation(collectionRelation = "personList")
public class Person extends RepresentationModel<Person> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long personId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Nullable
    @JsonIgnore
    @OneToMany(cascade = REMOVE, mappedBy = "person")
    private Collection<Address> addresses = new ArrayList<>();

    public Person(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personId, person.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }
}

