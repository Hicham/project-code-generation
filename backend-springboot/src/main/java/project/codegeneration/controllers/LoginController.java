package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.DTO.LoginRequest;
import project.codegeneration.models.DTO.LoginResponse;
import project.codegeneration.services.UserService;

@RestController
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;

    }

}
