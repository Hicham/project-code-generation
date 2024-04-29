package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.Cheese;
import project.codegeneration.models.Cow;
import project.codegeneration.repositories.CheeseRepository;
import project.codegeneration.repositories.CowRepository;


@Component
public class DataSeeder implements ApplicationRunner {

    private final CowRepository cowRepository;
    private final CheeseRepository cheeseRepository;

    public DataSeeder(CowRepository cowRepository, CheeseRepository cheeseRepository) {
        this.cowRepository = cowRepository;
        this.cheeseRepository = cheeseRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Add some data to the database
        Cow cow = new Cow();
        cow.setName("Betsy");

        cowRepository.save(cow);

        Cheese cheese = new Cheese();
        cheese.setName("Gouda");
        cheese.setPrice(5.99);
        cheese.setCow(cow);
        cheeseRepository.save(cheese);

    }
}
