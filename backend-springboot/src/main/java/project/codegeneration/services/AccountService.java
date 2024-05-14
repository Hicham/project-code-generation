package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.Account;
import project.codegeneration.models.User;
import project.codegeneration.repositories.AccountRepository;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElseThrow();
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

    public void deleteAccount(int id) {
        accountRepository.deleteById(id);
    }


}
