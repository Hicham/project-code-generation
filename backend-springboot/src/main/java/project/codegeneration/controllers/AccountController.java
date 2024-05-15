package project.codegeneration.controllers;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
//        return accounts.stream().map(account -> new AccountDTO(account.getIBAN(), account.getUserId().getUserId(), account.getAccountType(), account.getBalance())).toList();
//        return accounts.stream().map(account -> new AccountDTO(account.getIBAN(), account.getUser().getUserId(), account.getAccountType().toString(), account.getBalance())).toList();
        return accounts.stream().map(account -> new AccountDTO(account.getIBAN(), account.getAccountType().toString(), account.getBalance())).toList();
    }

    @GetMapping("/accounts/{IBAN}")
    public AccountDTO getAccountByIBAN(@PathVariable String IBAN, HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        String jwt = bearerToken.substring(7);

        System.out.println(jwt);

        Account account = accountService.getAccountByIBAN(IBAN);

        if (account == null) {
            return null;
        } else {
//            return new AccountDTO(account.getIBAN(), account.getUser().getUserId(), account.getAccountType().toString(), account.getBalance());
            return new AccountDTO(account.getIBAN(), account.getAccountType().toString(), account.getBalance());
        }
    }



}
