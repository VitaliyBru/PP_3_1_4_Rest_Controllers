package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.User;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @GetMapping("/user")
    public User getUserInf() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
