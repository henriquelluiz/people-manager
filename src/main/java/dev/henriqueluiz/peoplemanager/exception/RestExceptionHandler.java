package dev.henriqueluiz.peoplemanager.exception;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.exception.model.AbstractException;
import dev.henriqueluiz.peoplemanager.exception.model.RoleNotFoundException;
import dev.henriqueluiz.peoplemanager.exception.model.ValidationError;
import dev.henriqueluiz.peoplemanager.exception.model.ValidationField;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ValidationField> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ValidationField(err.getField(), err.getDefaultMessage()))
                .toList();
        ValidationError body = new ValidationError(status.value(), "Bad request", "One or more fields has an error.", fields);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Nullable
    @ExceptionHandler(value = RoleNotFoundException.class)
    protected ResponseEntity<Object> handleMethodRoleNotFound(RuntimeException ex, WebRequest request) {
        var body = new AbstractException();
        body.setStatus(404);
        body.setTitle("Not found");
        body.setDetails("Role not found");
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), NOT_FOUND, request);
    }

    @Nullable
    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleMethodUsernameNotFound(RuntimeException ex, WebRequest request) {
        var body = new AbstractException();
        body.setStatus(404);
        body.setTitle("Not found");
        body.setDetails("Username not found");
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), NOT_FOUND, request);
    }

    @Nullable
    @ExceptionHandler(value = BadCredentialsException.class)
    protected ResponseEntity<Object> handleMethodBadCredentials(RuntimeException ex, WebRequest request) {
        var body = new AbstractException();
        body.setStatus(401);
        body.setTitle("Bad credentials");
        body.setDetails("Username or password are not valid");
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), UNAUTHORIZED, request);
    }

    @Nullable
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleMethodEmailAlreadyExists(RuntimeException ex, WebRequest request) {
        var body = new AbstractException();
        body.setStatus(400);
        body.setTitle("Bad request");
        body.setDetails("Email or role name already exists");
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), BAD_REQUEST, request);
    }

    @Nullable
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleMethodEntityNotFound(RuntimeException ex, WebRequest request) {
        var body = new AbstractException();
        body.setStatus(404);
        body.setTitle("Not found");
        body.setDetails("Entity not found");
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), NOT_FOUND, request);
    }
}
