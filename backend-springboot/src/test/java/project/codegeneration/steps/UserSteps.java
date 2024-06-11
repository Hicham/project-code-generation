package project.codegeneration.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.codegeneration.CodegenerationApplication;
import project.codegeneration.controllers.UserController;
import project.codegeneration.models.DTO.UserDTO;
import project.codegeneration.services.UserService;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;

    @Autowired
    private UserController userController;

    @Given("user is given a user with email {string}")
    public void userIsGivenAUserWithEmail(String arg0) {

    }
}
