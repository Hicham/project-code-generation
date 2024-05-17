package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.services.AccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return accounts.stream().map(account -> new AccountDTO(account.getIBAN(), account.getUser(), account.getAccountType(), account.getBalance())).toList();
    }

    @GetMapping("/accounts/users/{email}")
    public AccountDTO getAccountsByUserEmail(@PathVariable String email) {
        Optional<Account> accounts = accountService.getAccountsByUserEmail(email);
        if (accounts.isPresent()) {
            Account account = accounts.get();
            return new AccountDTO(account.getIBAN(), account.getUser(), account.getAccountType(), account.getBalance());
        }
        else {
            return null;
        }

        }

}
