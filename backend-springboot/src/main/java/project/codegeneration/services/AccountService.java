package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;
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

    public Account getAccountByIBAN(String IBAN) {
        return accountRepository.findByIBAN(IBAN);
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

    public List<Account> getAccountsByUserId(int userId) {
                return accountRepository.findByUserId(userId);
    }


    public List<Account> getCheckingAccountsByUserId(Integer userId) {
        return accountRepository.findByUserIdAndAccountType(userId, AccountType.CHECKING);
    }

    public boolean deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return true;
    }
}
