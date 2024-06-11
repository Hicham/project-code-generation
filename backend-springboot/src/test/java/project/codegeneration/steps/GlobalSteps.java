package project.codegeneration.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import project.codegeneration.CodegenerationApplication;
import com.jayway.jsonpath.JsonPath;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class GlobalSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;

    private ResponseEntity<String> response;


    @Given("user is logged in as {string} with username {string} password {string}")
    public void userIsLoggedInAsWithUsernamePassword(String role ,String username, String password) {
        String loginUrl = "/login";

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        String loginRequestBody = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpEntity<String> loginEntity = new HttpEntity<>(loginRequestBody, loginHeaders);

        ResponseEntity<String> loginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, loginEntity, String.class);

        String token = JsonPath.read(loginResponse.getBody(), "$.token"); // Adjust the JSON path as per your response structure

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        sharedState.setHeaders(headers);
    }

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        HttpEntity<String> entity = new HttpEntity<>(sharedState.getHeaders());
        if ("GET".equalsIgnoreCase(method)) {
            response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, String.class);
        } else if ("POST".equalsIgnoreCase(method)) {
            response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
        } else if("PUT".equalsIgnoreCase(method)) {
            response = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class);
        }

        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Endpoint is not available");
    }

    @When("I access the endpoint {string} with method {string}")
    public void iAccessTheEndpointWithMethod(String endpoint, String method) {
        HttpHeaders headers = new HttpHeaders(sharedState.getHeaders());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        sharedState.setResponse(restTemplate.exchange(endpoint, HttpMethod.valueOf(method.toUpperCase()), entity, String.class));
    }

    @Then("I should receive status code {int}")
    public void iShouldReceiveStatusCode(int code) {
        assertEquals(HttpStatus.valueOf(code), sharedState.getResponse().getStatusCode());
        String responseBody = sharedState.getResponse().getBody();
        assertNotNull(responseBody);
    }

    @Given("headers are reset")
    public void headersAreReset() {
        sharedState.setHeaders(new HttpHeaders());
    }

    @When("I access the endpoint {string} with method {string} and body:")
    public void iAccessTheEndpointWithMethodAndBody(String endpoint, String method, String body) {
        HttpHeaders headers = new HttpHeaders(sharedState.getHeaders());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        sharedState.setResponse(restTemplate.exchange(endpoint, HttpMethod.valueOf(method.toUpperCase()), entity, String.class));
    }
}
