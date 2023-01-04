package dev.henriqueluiz.peoplemanager.web.response;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter @Setter
@AllArgsConstructor
@Relation(collectionRelation = "roleList")
public class RoleResponse extends RepresentationModel<RoleResponse> {
    private String name;
}
