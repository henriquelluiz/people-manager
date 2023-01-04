package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.web.request.UserRequest;
import dev.henriqueluiz.peoplemanager.web.response.AbstractResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class EntryPointController {

    @RequestMapping(
            value = "/",
            method = GET,
            produces = { "application/hal+json" }
    )
    public ResponseEntity<AbstractResponse> rootEntryPoint() {
        var model = new AbstractResponse();
        model.add(
                linkTo(methodOn(UserController.class)
                        .saveUser(new UserRequest("example@mail.com", "example")))
                        .withRel("createUser"));
        return ResponseEntity.ok(model);
    }
}
