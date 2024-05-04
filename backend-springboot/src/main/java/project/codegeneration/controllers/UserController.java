package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.DTO.UserDTO;
import project.codegeneration.models.User;
import project.codegeneration.services.UserService;

import java.util.List;

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
        return users.stream().map(user -> new UserDTO(user.getUserId(), user.getRoleId().getRoleName(), user.isIsApproved(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getBSNNumber(), user.getPhoneNumber(), user.getPinCode())).toList();
    }
}
