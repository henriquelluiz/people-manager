package dev.henriqueluiz.peoplemanager.model;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "addressList")
public class Address extends RepresentationModel<Address> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long addressId;

    @NotBlank
    private String street;

    @NotBlank
    @Length(min = 8, max = 8)
    private String postalCode;

    @NotBlank
    private String number;

    @NotBlank
    private String city;

    @NotNull
    private Boolean preferred = false;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "personId")
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(addressId, address.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId);
    }
}
