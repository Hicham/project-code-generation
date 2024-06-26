package project.codegeneration.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.DTO.ApproveUserDTO;
import project.codegeneration.models.DTO.UserDTO;
import project.codegeneration.models.DTO.UserRegisterDTO;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    private final AccountService accountService;

    public UserController(final UserService userService, final AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getUsers(@RequestParam(required = false, defaultValue = "0") Integer pageNumber, @RequestParam(required = false, defaultValue = "") String email) {

        Page<User> users;

        Pageable pageable = PageRequest.of(pageNumber, 10);

        users = userService.getUsers(pageable, email);


        Page<UserDTO> userDTOpage = users.map(user -> new UserDTO(
                user.getId(),
                user.getRoles().toString(),
                user.isApproved(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBSNNumber(),
                user.getPhoneNumber()
        ));

        return ResponseEntity.ok(userDTOpage);


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/unapproved")
    public List<UserDTO> getNotApprovedUsers() {
        List<User> users = userService.getNotApprovedUsers();
        return users.stream().map(user -> new UserDTO(user.getId(), user.getRoles().toString(), user.isApproved(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getBSNNumber(), user.getPhoneNumber())).toList();
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegisterDTO userDTO) {
        try {
            User user = new User(
                    List.of(Role.ROLE_USER), // Assuming roles should be an empty list
                    false, // Assuming approved status is false
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/{id}/approve")
    public ResponseEntity<String> approveUser(@RequestBody ApproveUserDTO request, @PathVariable String id) {
        try {
            long userId = Long.parseLong(id);
            userService.approveUser(userId);
            accountService.createAccountForApprovedUser(userService.getUserById(userId).orElseThrow(), request);
            return ResponseEntity.ok("User approved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated() and #email == principal.username or hasRole('ROLE_ADMIN')")
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
                    user.getFirstName(),
                    user.getLastName(),
                    user.getBSNNumber(),
                    user.getPhoneNumber()
            );
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
