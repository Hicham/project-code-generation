package project.codegeneration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/cucumber", // Path to your feature files
        glue = "project.codegeneration.steps",   // Package with step definitions
        plugin = {"pretty", "html:target/cucumber-report.html"}, // Plugins for reporting
        monochrome = true
)
//@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RunCucumberTest {
}
