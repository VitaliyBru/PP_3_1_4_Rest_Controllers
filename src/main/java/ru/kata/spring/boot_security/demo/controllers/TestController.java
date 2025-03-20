package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class TestController {
    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String initPage(Model model) {
        User user = null;
        if (userService.getAllUser().isEmpty()) {
            user = new User();
            user.setPassword("admin");
            user.setAge(31);
            user.setEmail("admin@admin");
            user.setFirstName("admin");
            user.setLastName("admin");
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ROLE_ADMIN);
            roles.add(Role.ROLE_USER);
            user.setRoles(roles);
            userService.saveUser(user);
        }
        model.addAttribute("user", user);
        return "init";
    }
}
