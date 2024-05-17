package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.services.AccountService;

import java.util.List;

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
        return accounts.stream().map(account -> new AccountDTO(account.getIBAN(), account.getUserId().getUserId(), account.getAccountType(), account.getBalance())).toList();
    }

}
