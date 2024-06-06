package project.codegeneration.controllers;

import org.springframework.security.core.Authentication;
import project.codegeneration.models.User;
import project.codegeneration.services.UserService;

import java.util.Optional;

public class Controller {
    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getCurrentUser(Authentication authentication) {
        String currentUsername = authentication.getName();
        return userService.findByEmail(currentUsername);
    }
}
