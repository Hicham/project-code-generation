package project.codegeneration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.Account;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.CheeseRepository;
import project.codegeneration.repositories.CowRepository;
import project.codegeneration.services.AccountCardService;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;


@Component
public class DataSeeder implements ApplicationRunner {

    private final CowRepository cowRepository;
    private final CheeseRepository cheeseRepository;
    private final AccountRepository accountRepository;

    private final AccountService accountService;
    private final AccountCardService accountCardService;
    private UserService userService;


       public DataSeeder(CowRepository cowRepository, CheeseRepository cheeseRepository, AccountRepository accountRepository, AccountService accountService, AccountCardService accountCardService, UserService userService) {
           this.cowRepository = cowRepository;
           this.cheeseRepository = cheeseRepository;
           this.accountRepository = accountRepository;
           this.accountService = accountService;
           this.accountCardService = accountCardService;
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

//        User user = new User(List.of(Role.ROLE_USER), false, "Hicham@gmail.com", "test", "test", "test", "3652584", "06352615");
//        userService.create(user);
//
////        User user = userService.findByEmail("Hicham@gmail.com").get();
////
//        AccountCard card = new AccountCard("0000", "Hicham El Ans");
////
//        accountCardService.create(card);
//
//        List<AccountCard> cards = new ArrayList<>();
//        cards.add(card);
//
//        Account account = new Account("IBANFAKE1", user, AccountType.SAVINGS, 1000, cards);
//
//        accountRepository.save(account);


//            Account account = accountService.getAccountByIBAN("IBANFAKE1");
//            System.out.println(account);


    }
}
