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

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@Relation(collectionRelation = "userList")
public class UserResponse extends RepresentationModel<UserResponse> {
    private String email;
    private List<String> authorities;
}
