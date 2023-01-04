package dev.henriqueluiz.peoplemanager.exception.model;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationError extends AbstractException {
    private final List<ValidationField> fields;

    public ValidationError(Integer status, String title, String details, List<ValidationField> fields) {
        super(status, title, details);
        this.fields = fields;
    }
}
