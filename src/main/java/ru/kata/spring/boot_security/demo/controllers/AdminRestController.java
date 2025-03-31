package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserInvalidFieldsException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final UserService userService;
    private final UserValidator userValidator;

    public AdminRestController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/people")
    public List<User> getPeople() {
        return userService.getAllUser();
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    @GetMapping("/all-roles")
    public Role[] getAllRoles() {
        return Role.values();
    }

    @PostMapping(value = "/save-user")
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errMsg = new StringBuilder();
            bindingResult.getFieldErrors().forEach(fieldError -> errMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(";\n")
            );
            throw new UserInvalidFieldsException(errMsg.toString());
        }

        userService.saveUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value = "/delete-user")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody User user) {
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserInvalidFieldsException e) {
        return new ResponseEntity<>(new UserErrorResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
