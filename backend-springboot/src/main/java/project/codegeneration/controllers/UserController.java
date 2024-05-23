package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.Cow;
import project.codegeneration.models.DTO.CowDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.Cow;
import project.codegeneration.models.DTO.CowDTO;
import project.codegeneration.models.DTO.UserDTO;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    private final AccountService accountService;

    public UserController(final UserService userService, final AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        List<User> users = userService.getAllUsers();
<<<<<<< HEAD

        return users.stream().map(user -> new UserDTO(
                user.getId(),
                user.getRoles().toString(),
                user.isApproved(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getBSNNumber(),
                user.getPhoneNumber()
        )).toList();

=======
        return users.stream().map(user -> new UserDTO(user.getId(), user.getRoles().toString(), user.isApproved(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getBSNNumber(), user.getPhoneNumber())).toList();
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)
    }

    @GetMapping("/unapproved-users")
    public List<UserDTO> getNotApprovedUsers() {
        List<User> users = userService.getNotApprovedUsers();
        return users.stream().map(user -> new UserDTO(user.getId(), user.getRoles().toString(), user.isApproved(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getBSNNumber(), user.getPhoneNumber())).toList();
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        try {
            User user = new User(
<<<<<<< HEAD
                    List.of(), // Assuming roles should be an empty list
                    false, // Assuming approved status is false
=======
//                    userDTO.getUserId(),
                    List.of(), // Set default roles or parse from DTO if necessary
                    false, // Set default approval status or parse from DTO if necessary
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getBSNNumber(),
                    userDTO.getPhoneNumber()
            );

            userService.create(user);
            return "User registered successfully";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

<<<<<<< HEAD
    @GetMapping("/users/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDTO(
                    user.getId(),
                    user.getRoles().toString(),
                    user.isApproved(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBSNNumber(),
                    user.getPhoneNumber()
            );
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
=======
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)

    @PostMapping("/accounts")
    public ResponseEntity<String> approveUser(@RequestParam("userId") int userId) {
        try {
            userService.approveUser(userId);
            accountService.createAccountForApprovedUser(userService.getUserById(userId));
            return ResponseEntity.ok("User approved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
