package project.codegeneration.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import project.codegeneration.CodegenerationApplication;

@CucumberContextConfiguration
@SpringBootTest(classes = CodegenerationApplication.class)
public class CucumberSpringConfiguration {
}
