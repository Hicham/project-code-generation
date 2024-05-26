package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {


    private final AccountRepository accountRepository;

    private final AccountService accountService;
    private UserService userService;


    public DataSeeder(AccountRepository accountRepository, AccountService accountService, UserService userService) {
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
//        User user = new User(List.of(Role.ROLE_USER), true, "test2@gmail.com", "test", "test", "test", "3652584", "06352615");
//////        User user = new User(List.of(Role.ROLE_USER), false, "hicham2@gmail.com", "test", "test", "test", "3652584", "06352615");
//        userService.create(user);
//
//
//////
//        Account account = new Account("IBANFAKE2", user ,AccountType.CHECKING, 1000, true, 999999999);
//        Account account2 = new Account("IBANFAKE3", user , AccountType.SAVINGS, 2000, true, 999999999);
////
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
