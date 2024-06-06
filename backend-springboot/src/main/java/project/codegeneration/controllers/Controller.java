package project.codegeneration.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import project.codegeneration.models.User;
import project.codegeneration.services.UserService;

import java.util.Optional;

public class Controller {
    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userService.getUserByEmail(userDetails.getUsername());
    }
}
