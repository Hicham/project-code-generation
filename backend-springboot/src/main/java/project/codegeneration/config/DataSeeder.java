package project.codegeneration.config;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import project.codegeneration.models.Account;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.ArrayList;
import java.util.List;


@Component
public class DataSeeder implements ApplicationRunner {


    private AccountService accountService;
    private UserService userService;

    public DataSeeder(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }




    @Override
    public void run(ApplicationArguments args) throws Exception {

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





    }
}
