package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.Cheese;
import project.codegeneration.models.Cow;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.repositories.CheeseRepository;
import project.codegeneration.repositories.CowRepository;
import project.codegeneration.services.UserService;

import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {

    private final CowRepository cowRepository;
    private final CheeseRepository cheeseRepository;
    private UserService userService;


       public DataSeeder(CowRepository cowRepository, CheeseRepository cheeseRepository, UserService userService) {
            this.cowRepository = cowRepository;
            this.cheeseRepository = cheeseRepository;
            this.userService = userService;
        }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Add some data to the database
        Cow cow = new Cow();
        cow.setName("Betsy");
        cow.setAge(5);


        cowRepository.save(cow);

        Cheese cheese = new Cheese();
        cheese.setName("Gouda");
        cheese.setPrice(5.99);
        cheese.setAge(3);
        cheese.setCow(cow);
        cheeseRepository.save(cheese);

//        User user = new User(1, List.of(Role.ROLE_USER), false, "duha@gmail.com", "test", "Duha", "Kahya", "3652584", "06352615", 2514);
//        User user2 = new User(2, List.of(Role.ROLE_USER), false, "wouter@gmail.com", "test", "Wouter", "Zwart", "3652666", "06352666", 2556);
//        User user3 = new User(3, List.of(Role.ROLE_USER), true, "woutert@gmail.com", "test", "Wouter", "Zwart", "3652666", "06352666", 2557);
//        userService.create(user);
//        userService.create(user2);
//        userService.create(user3);
    }
}
