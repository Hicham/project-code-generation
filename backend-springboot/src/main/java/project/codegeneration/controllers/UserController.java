package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.Cow;
import project.codegeneration.models.DTO.CowDTO;
import project.codegeneration.models.DTO.UserDTO;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(user -> new UserDTO(
                user.getUserId(),
                user.getRoles().toString(),
                user.isApproved(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getBSNnumber(),
                user.getPhoneNumber(),
                user.getPinCode()
        )).toList();
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        try {
            User user = new User(
                    userDTO.getUserId(),
                    List.of(), // Set default roles or parse from DTO if necessary
                    false, // Set default approval status or parse from DTO if necessary
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getBSNNumber(),
                    userDTO.getPhoneNumber(),
                    userDTO.getPinCode()
            );

            userService.create(user);
            return "User registered successfully";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/users/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDTO(
                    user.getUserId(),
                    user.getRoles().toString(),
                    user.isApproved(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBSNnumber(),
                    user.getPhoneNumber(),
                    user.getPinCode()
            );
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


}
