package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.*;
import project.codegeneration.models.DTO.ApproveUserDTO;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.TransactionLimitRepository;
import project.codegeneration.repositories.UserRepository;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionService;
import project.codegeneration.services.UserService;

import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionLimitRepository transactionLimitRepository;

    public DataSeeder(AccountService accountService, AccountRepository accountRepository, UserRepository userRepository, UserService userService, TransactionService transactionService, TransactionLimitRepository transactionLimitRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionLimitRepository = transactionLimitRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("DataSeeder is running...");
        User user = new User(List.of(Role.ROLE_ADMIN), true, "hicham@gmail.com", "Test123", "test", "test", "3652584", "06352615");
        userService.create(user);


        createAccountWithTransactionLimit(user, "IBANFAKE1", 1000.00, AccountType.CHECKING, 0, 99999999);
        createAccountWithTransactionLimit(user, "IBANFAKE2", 1000.00, AccountType.SAVINGS, 0, 99999999);


        User user2 = new User(List.of(Role.ROLE_USER), true, "user@gmail.com", "Test123", "test", "test", "3652584", "06352615");
        userService.create(user2);

        User user3 = new User(List.of(Role.ROLE_USER), false, "user3@gmail.com", "Test123", "test", "test", "3652584", "06352615");
        userService.create(user3);

        createAccountWithTransactionLimit(user2, "IBANFAKE3", 1000.00, AccountType.CHECKING,  0, 20000);
        createAccountWithTransactionLimit(user2, "IBANFAKE4", 1000.00, AccountType.SAVINGS, 0, 20000);

        transactionService.ATMTransaction("IBANFAKE3", null, 50.00, "Withdrawal", TransactionType.WITHDRAW, user2);

//        accountService.createAccountForApprovedUser(user2, new ApproveUserDTO(2, new TransactionLimitDTO("IBANFAKE2", 1000, 0), 0));

//        transactionService.ATMTransaction("NL23INHO1813601517", null, 1500.00, "Withdrawal", TransactionType.WITHDRAW, user);

//        System.out.println("DataSeeder is running...");
//
//
//        System.out.println(userRepository.findByEmail("hicham@gmail.com"));
//
//        if (userRepository.findByEmail("hicham@gmail.com").isEmpty()) {
//            User user = new User(List.of(Role.ROLE_ADMIN), true, "hicham@gmail.com", "Test123", "test", "test", "3652584", "06352615");
//            userRepository.saveAndFlush(user);
//            System.out.println("User created");
//        }
//        else
//        {
//            System.out.println("User already exists");
//        }




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

////
//
//////
//        Account account = new Account("IBANFAKE2", user ,AccountType.CHECKING, 1000, true, 999999999);
//        Account account2 = new Account("IBANFAKE3", user , AccountType.SAVINGS, 2000, true, 999999999);
////
//        accountRepository.save(account);
//        accountRepository.save(account2);
////
//
//       User user1 = new User(List.of(Role.ROLE_ADMIN), false, "wouter1234@gmail.com", "Test123", "test", "test", "3652574", "06352615");
//        User user2 = new User(List.of(Role.ROLE_USER), true, "duha1@gmail.com", "Test123", "test", "test", "3652584", "06352615");
//        User user3 = new User(List.of(Role.ROLE_USER), false, "hicham25@gmail.com", "Test123", "test", "test", "3652584", "06352615");
//        User user4 = new User(List.of(Role.ROLE_USER), false, "hicham26@gmail.com", "Test123", "test", "test", "3652584", "06352615");
//        User user5 = new User(List.of(Role.ROLE_USER), false, "hicham247gmail.com", "Test123", "test", "test", "3652584", "06352615");
//                userService.create(user1);
//
//
//       userService.create(user2);
//         userService.create(user3);
//            userService.create(user4);
//            userService.create(user5);
////
//        TransactionLimit transactionLimit = new TransactionLimit("IBANFAKE1", 1000);
//
//
//        transactionLimit.setAccount(accountService.getAccountByIBAN("IBANFAKE1"));
//
//        Account account = new Account("IBANFAKE1", transactionLimit, user2 ,AccountType.CHECKING, 1000, true, 999999999);
//        Account account2 = new Account("IBANFAKE2", transactionLimit, user2 , AccountType.SAVINGS, 2000, true, 999999999);
//
//
//
//       accountService.createAccount(account);
//        accountRepository.save(account);
//
//        accountService.createAccount(account2);
//        accountRepository.save(account2);


//            Account account = accountService.getAccountByIBAN("IBANFAKE1");
//            System.out.println(account);


//        String email = accountCardRepository.findUserEmailByCardId(1);
//
//        System.out.println(email);

    }

    public void createAccountWithTransactionLimit(User user, String IBAN, Double balance, AccountType accountType, double absoluteLimit, double dailyLimit) {
        // Create and set up a new account
        Account account = new Account();
        account.setIBAN(IBAN);
        account.setAccountType(accountType);
        account.setBalance(balance);
        account.setActive(true);
        account.setAbsoluteLimit(absoluteLimit);
        account.setUser(user);
        accountRepository.save(account);

        // Create and set up a new transaction limit
        TransactionLimit transactionLimit = new TransactionLimit();
        transactionLimit.setIBAN(account.getIBAN());
        transactionLimit.setAccount(account);
        transactionLimit.setDailyLimit(dailyLimit);
        transactionLimitRepository.save(transactionLimit);

        // Associate the transaction limit with the account
        account.setTransactionLimit(transactionLimit);
        accountRepository.save(account);
    }

}
