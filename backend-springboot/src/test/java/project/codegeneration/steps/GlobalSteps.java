package project.codegeneration.steps;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import project.codegeneration.CodegenerationApplication;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GlobalSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;

    @Given("user is logged in as admin with username {string} password {string}")
    public void userIsLoggedInAsAdminWithUsernamePassword(String username, String password) {
        String loginUrl = "/login";

        // Create login request
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        String loginRequestBody = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpEntity<String> loginEntity = new HttpEntity<>(loginRequestBody, loginHeaders);

        // Send login request
        ResponseEntity<String> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginEntity, String.class);

        // Extract token from login response
        String token = JsonPath.read(loginResponse.getBody(), "$.token"); // Adjust the JSON path as per your response structure

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // Set headers in shared state
        sharedState.setHeaders(headers);
    }
}
