package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.*;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.UserRepository;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;
    private final UserService userService;

    public DataSeeder(AccountService accountService, AccountRepository accountRepository, UserRepository userRepository, UserService userService) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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

//       User user = new User(List.of(Role.ROLE_ADMIN), false, "wouter123@gmail.com", "test", "test", "test", "3652574", "06352615");
//        User user2 = new User(List.of(Role.ROLE_USER), false, "hicham21@gmail.com", "test", "test", "test", "3652584", "06352615");
//        User user3 = new User(List.of(Role.ROLE_USER), false, "hicham22@gmail.com", "test", "test", "test", "3652584", "06352615");
//        User user4 = new User(List.of(Role.ROLE_USER), false, "hicham23@gmail.com", "test", "test", "test", "3652584", "06352615");
//        User user5 = new User(List.of(Role.ROLE_USER), false, "hicham24@gmail.com", "test", "test", "test", "3652584", "06352615");
//                userService.create(user);
//
//
//       userService.create(user2);
//         userService.create(user3);
//            userService.create(user4);
//            userService.create(user5);
//
//        TransactionLimit transactionLimit = new TransactionLimit("IBANFAKE1", 1000, 10000, 100000);
//
//        Account account = new Account("IBANFAKE1", user , AccountType.CHECKING, 1000, true, 999999999);
//
//        //Account account = new Account("IBANFAKE2", user , AccountType.CHECKING, 1000, true, 999999999);
//        //Account account2 = new Account("IBANFAKE3", user ,AccountType.CHECKING, 2000, true, 999999999);
//
//       accountService.createAccount(account);
        //accountRepository.save(account2);


//            Account account = accountService.getAccountByIBAN("IBANFAKE1");
//            System.out.println(account);


//        String email = accountCardRepository.findUserEmailByCardId(1);
//
//        System.out.println(email);

    }
}
