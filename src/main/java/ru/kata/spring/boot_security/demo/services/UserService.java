package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user) {
        if (user.getId() == 0 || !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            userRepository.findById(user.getId()).ifPresent(u -> user.setPassword(u.getPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    public boolean checkEmailIsNotUniq(String email, int id) {
        return userRepository.findByEmailAndIdNot(email, id).isPresent();
    }
}
