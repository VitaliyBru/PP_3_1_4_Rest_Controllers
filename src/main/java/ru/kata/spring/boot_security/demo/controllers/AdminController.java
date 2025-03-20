package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String adminPage(Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        model.addAttribute("ADMIN", Role.ROLE_ADMIN);
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("users", userService.getAllUser());
        return "admin/admin";
    }

    @GetMapping("/addNewUser")
    public String newUserPage(Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        model.addAttribute("ADMIN", Role.ROLE_ADMIN);
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        return "admin/addNewUser";
    }

    @PostMapping("/addNewUser")
    public String newUserProcessing(@ModelAttribute("newUser") @Valid User newUser, BindingResult bindingResult) {
        userValidator.validate(newUser, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/addNewUser";
        }
        userService.saveUser(newUser);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("user_id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("user_id") Integer id, Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Optional<User> person = userService.getUserById(id);
        if (person.isEmpty()) {
            return "redirect:/admin";
        }
        person.get().setPassword("");
        model.addAttribute("ADMIN", Role.ROLE_ADMIN);
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("editedUser", person.get());
        model.addAttribute("roles", Role.values());
        return "admin/editUser";
    }

    @PostMapping("/edit")
    public String editUserProcessing(@ModelAttribute("editedUser") @Valid User editedUser, BindingResult bindingResult, Model model) {
        userValidator.validate(editedUser, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "admin/editUser";
        }
        userService.updateUser(editedUser);
        return "redirect:/admin";
    }
}
