package project.codegeneration.config;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import project.codegeneration.models.*;
import project.codegeneration.repositories.CowRepository;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import project.codegeneration.models.Account;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;


import java.util.ArrayList;
import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private AccountService accountService;
    private UserService userService;

    public DataSeeder(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {


//        User user = new User(List.of(Role.ROLE_USER), false, "hicham@gmail.com", "test", "test", "test", "3652584", "06352615");
//        User user = new User(List.of(Role.ROLE_USER), false, "hicham2@gmail.com", "test", "test", "test", "3652584", "06352615");
//        userService.create(user);

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setBSNnumber("123456789");
        user.setPhoneNumber("123456789");
        user.setPinCode(1234);

        userService.create(user);

        Account account = new Account();
        account.setIBAN("NL01INHO0000000001");
        account.setUser(user);
        account.setAccountType("Savings");
        account.setBalance(1000);

        accountService.createAccount(account);




//        User user = userService.findByEmail("hicham@gmail.com").get();
//
//        Account account = new Account("IBANFAKE2", user ,AccountType.CHECKING, 1000, true, 999999999);
//        Account account2 = new Account("IBANFAKE3", user ,AccountType.CHECKING, 2000, true, 999999999);
//
//        accountRepository.save(account);
//        accountRepository.save(account2);
//

//            Account account = accountService.getAccountByIBAN("IBANFAKE1");
//            System.out.println(account);


//        String email = accountCardRepository.findUserEmailByCardId(1);
//
//        System.out.println(email);

    }
}
