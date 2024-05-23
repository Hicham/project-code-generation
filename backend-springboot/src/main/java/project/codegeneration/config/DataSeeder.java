package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.*;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.CheeseRepository;
import project.codegeneration.repositories.CowRepository;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Component
public class DataSeeder implements ApplicationRunner {

    private final CowRepository cowRepository;
    private final CheeseRepository cheeseRepository;
    private final AccountRepository accountRepository;

    private final AccountService accountService;
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
//                User user = userService.findByEmail("hicham@gmail.com").get();
//
//        Random random = new Random();
//
//        for (int i = 0; i < 10; i++) {
//            String iban = "IBAN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Generate a random IBAN
//            int balance = random.nextInt(10000); // Random balance between 0 and 9999
//            Account account = new Account(iban, user, AccountType.CHECKING, balance, true, 999999999);
//            accountRepository.save(account);
//        }

//        User user = new User(List.of(Role.ROLE_USER), false, "hicham@gmail.com", "test", "test", "test", "3652584", "06352615");
//        User user = new User(List.of(Role.ROLE_USER), false, "hicham2@gmail.com", "test", "test", "test", "3652584", "06352615");
//        userService.create(user);


////
//        Account account = new Account("IBANFAKE2", user ,AccountType.CHECKING, 1000, true, 999999999);
//        Account account2 = new Account("IBANFAKE3", user ,AccountType.CHECKING, 2000, true, 999999999);
//
//        accountRepository.save(account);
//        accountRepository.save(account2);
////

//            Account account = accountService.getAccountByIBAN("IBANFAKE1");
//            System.out.println(account);


//        String email = accountCardRepository.findUserEmailByCardId(1);
//
//        System.out.println(email);

    }
}
