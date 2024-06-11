package project.codegeneration.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import project.codegeneration.CodegenerationApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CodegenerationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SharedState sharedState;


    @Then("I should receive the checking accounts for user {int}")
    public void iShouldReceiveTheCheckingAccountsForUser(int id) {
        assertEquals(HttpStatus.OK, sharedState.getResponse().getStatusCode());
        String responseBody = sharedState.getResponse().getBody();
        Integer userId = JsonPath.parse(responseBody).read("$.content[0].user.id", Integer.class);
        assertEquals(id, userId);
    }
}
