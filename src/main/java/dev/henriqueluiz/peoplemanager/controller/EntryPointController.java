package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.web.response.AbstractResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(model);
    }
}
