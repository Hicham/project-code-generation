package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.DTO.LoginRequest;
import project.codegeneration.models.DTO.LoginRequestCard;
import project.codegeneration.models.DTO.LoginResponse;
import project.codegeneration.services.AccountCardService;
import project.codegeneration.services.UserService;

@RestController
public class LoginController {

    private final UserService userService;
    private final AccountCardService accountCardService;

    public LoginController(UserService userService, AccountCardService accountCardService) {
        this.userService = userService;
        this.accountCardService = accountCardService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){

        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;

    }

    @PostMapping("atm/login")
    public LoginResponse loginCard(@RequestBody LoginRequestCard loginRequestCard){

        String token = accountCardService.login((loginRequestCard.getId()), loginRequestCard.getPincode());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;

    }
}
