package project.codegeneration.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.codegeneration.CodegenerationApplication;
import project.codegeneration.steps.SharedState;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;

    private ResponseEntity<String> responseEntity;

    @Then("I should receive transactions of iban {string}")
    public void receiveTransactionsOfIBAN(String iban) {
        // Your implementation here
    }
}
