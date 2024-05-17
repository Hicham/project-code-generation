package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.Account;
import project.codegeneration.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }


    public Optional<Account> getAccountsByUserEmail(String email) {
        return accountRepository.findAccountsByUserEmail(email);
    }

}
