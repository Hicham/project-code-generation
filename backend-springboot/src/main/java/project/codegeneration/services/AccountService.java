package project.codegeneration.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;
import project.codegeneration.models.TransactionType;
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


//    @Transactional
//    public boolean deposit(Account account, double amount) {
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Deposit amount must be positive");
//        }
//
//        transactionService.createATMTransaction(null, account.getIBAN(), amount, TransactionType.DEPOSIT, account.getUser());
//
//        account.setBalance(account.getBalance() + amount);
//        accountRepository.save(account);
//        accountRepository.flush();
//        return true;
//    }
//
//    @Transactional
//    public boolean withdraw(Account account, double amount) {
//        if (amount <= 0) {
//            throw new IllegalArgumentException("Withdraw amount must be positive");
//        }
//
//        transactionService.createATMTransaction(account.getIBAN(), null, amount, TransactionType.WITHDRAW, account.getUser());
//
//        account.setBalance(account.getBalance() - amount);
//        accountRepository.save(account);
//        accountRepository.flush();
//        return true;
//    }

    public void deposit(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        accountRepository.flush();
    }


    public void withdraw(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.flush();
    }
}
