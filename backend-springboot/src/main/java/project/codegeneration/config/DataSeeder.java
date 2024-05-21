package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.*;
<<<<<<< HEAD
=======
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.CheeseRepository;
import project.codegeneration.repositories.CowRepository;
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {

<<<<<<< HEAD
    private AccountService accountService;
=======
    private final CowRepository cowRepository;
    private final CheeseRepository cheeseRepository;
    private final AccountRepository accountRepository;

    private final AccountService accountService;
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)
    private UserService userService;


       public DataSeeder(CowRepository cowRepository, CheeseRepository cheeseRepository, AccountRepository accountRepository, AccountService accountService, UserService userService) {
           this.cowRepository = cowRepository;
           this.cheeseRepository = cheeseRepository;
           this.accountRepository = accountRepository;
           this.accountService = accountService;

           this.userService = userService;
        }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Add some data to the database
//        Cow cow = new Cow();
//        cow.setName("Betsy");
//        cow.setAge(5);
//
//
//        cowRepository.save(cow);
//
//        Cheese cheese = new Cheese();
//        cheese.setName("Gouda");
//        cheese.setPrice(5.99);
//        cheese.setAge(3);
//        cheese.setCow(cow);
//        cheeseRepository.save(cheese);

//       User user = new User(List.of(Role.ROLE_USER), false, "hichamm@gmail.com", "test", "test", "test", "3652584", "06352615");
//        User user = new User(List.of(Role.ROLE_USER), false, "hicham2@gmail.com", "test", "test", "test", "3652584", "06352615");
<<<<<<< HEAD
        //        userService.create(user);


=======
//        userService.create(user);
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)

//        User user = userService.findByEmail("hicham@gmail.com").get();
//
//        Account account = new Account("IBANFAKE2", user ,AccountType.CHECKING, 1000, true, 999999999);
//        Account account2 = new Account("IBANFAKE3", user ,AccountType.CHECKING, 2000, true, 999999999);
//
 //      accountService.createAccount(account);
//        accountRepository.save(account2);
//

//            Account account = accountService.getAccountByIBAN("IBANFAKE1");
//            System.out.println(account);


//        String email = accountCardRepository.findUserEmailByCardId(1);
//
//        System.out.println(email);

    }
}
