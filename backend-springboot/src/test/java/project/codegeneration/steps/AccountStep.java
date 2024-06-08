package project.codegeneration.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import project.codegeneration.CodegenerationApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class AccountStep {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;

    private ResponseEntity<String> response;

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        // Placeholder for any setup required
    }

    @When("I retrieve all accounts")
    public void iRetrieveAllAccounts() {
        HttpEntity<String> entity = new HttpEntity<>(sharedState.getHeaders());
        response = restTemplate.exchange("/api/accounts", HttpMethod.GET, entity, String.class);
    }

    @Then("I should receive all accounts")
    public void iShouldReceiveAllAccounts() {
        System.out.println("response: " + response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody);

        // Further assertions can be added to validate the returned accounts
    }
}
