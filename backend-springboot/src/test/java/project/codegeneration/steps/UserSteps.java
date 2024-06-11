package project.codegeneration.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.codegeneration.CodegenerationApplication;
import project.codegeneration.controllers.UserController;
import project.codegeneration.models.DTO.UserDTO;
import project.codegeneration.services.UserService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class UserSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;

    @And("I should receive a list of unapproved users")
    public void iShouldReceiveAListOfUnapprovedUsers() {
        assertEquals(HttpStatus.OK, sharedState.getResponse().getStatusCode());
    }

    @And("the response body should contain {string}")
    public void theResponseBodyShouldContain(String arg0) {
        String responseBody = sharedState.getResponse().getBody();
        assertEquals(arg0, responseBody);
    }
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private SharedState sharedState;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserController userController;
//
//    @Given("I have a user with email {string}")
//    public void iHaveAUserWithEmail(String email) {
//        UserDTO userDTO = new UserDTO(1, "USER", true, email, "Test123", "John", "Doe", "0612345678", "123456789");
//        ResponseEntity<String> response = restTemplate.postForEntity("/api/register", userDTO, String.class);
//        sharedState.setResponse(response);
//    }
//
//    @When("I request all users")
//    public void iRequestAllUsers() {
//        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity("/api/users", UserDTO[].class);
//        UserDTO[] users = response.getBody();
//        String responseBody = Arrays.toString(users); // Convert array to JSON string
//        ResponseEntity<String> stringResponse = new ResponseEntity<>(responseBody, response.getHeaders(), response.getStatusCode());
//        sharedState.setResponse(stringResponse);
//    }
//
//
//    @Then("I should receive a list of users")
//    public void iShouldReceiveAListOfUsers() throws IOException {
//        assertEquals(HttpStatus.OK, sharedState.getResponse().getStatusCode());
//        String responseBody = sharedState.getResponse().getBody();
//        assertNotNull(responseBody);
//
//        // Convert JSON string to array of UserDTO objects
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDTO[] users = objectMapper.readValue(responseBody, UserDTO[].class);
//
//        assertNotNull(users);
//    }
//
//    @And("the response body should contain {string}")
//    public void theResponseBodyShouldContain(String email) throws IOException {
//        String responseBody = sharedState.getResponse().getBody();
//        assertNotNull(responseBody);
//
//        // Convert JSON string to array of UserDTO objects
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDTO[] users = objectMapper.readValue(responseBody, UserDTO[].class);
//
//        assertNotNull(users);
//
//        // Check if any user's email matches the provided email
//        boolean emailExists = false;
//        for (UserDTO user : users) {
//            if (user.getEmail().equals(email)) {
//                emailExists = true;
//                break;
//            }
//        }
//        assertTrue(emailExists);
//    }


}

    

