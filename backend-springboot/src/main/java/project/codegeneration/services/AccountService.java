package project.codegeneration.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.codegeneration.models.*;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.TransactionLimitRepository;
import project.codegeneration.util.IBANGenerator;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final TransactionLimitRepository transactionLimitRepository;

    public AccountService(AccountRepository accountRepository, TransactionLimitRepository transactionLimitRepository) {
        this.accountRepository = accountRepository;
        this.transactionLimitRepository = transactionLimitRepository;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account getAccountByIBAN(String IBAN) {
        return accountRepository.findByIBAN(IBAN);
    }


    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
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

    public Page<Account> getAccountsByUserId(Pageable pageable, int userId) {
        return accountRepository.findByUserId(pageable, userId);
    }

    public Optional<Account> getAccountsByUserEmail(String email) {
        return accountRepository.findAccountsByUserEmail(email);
    }


    public Page<Account> getCheckingAccountsByUserId(Pageable pageable, Integer userId) {
        return accountRepository.findByUserIdAndAccountType(pageable, userId, AccountType.CHECKING);
    }


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
    public void createAccountForApprovedUser(User user, TransactionLimitDTO transactionLimitDTO){
        if(user.isApproved()){
            // Generate unique IBAN
            String checkingIBAN = generateUniqueIBAN();
            String savingsIBAN = generateUniqueIBAN();

            // Create checking account
            Account checkingAccount = new Account();
            checkingAccount.setIBAN(checkingIBAN);
            checkingAccount.setAccountType(AccountType.CHECKING);
            checkingAccount.setBalance(0);
            checkingAccount.setActive(true);
            checkingAccount.setAbsoluteLimit(0);
            checkingAccount.setUser(user);

            // Save checking account
            accountRepository.save(checkingAccount);

            // Create transaction limit for checking account
            TransactionLimit transactionLimit = new TransactionLimit();
            transactionLimit.setIBAN(checkingIBAN);
            transactionLimit.setAccount(checkingAccount);
            transactionLimit.setDailyLimit(transactionLimitDTO.getDailyLimit());
            transactionLimit.setWeeklyLimit(transactionLimitDTO.getWeeklyLimit());
            transactionLimit.setMonthlyLimit(transactionLimitDTO.getMonthlyLimit());

            // Save transaction limit for checking account
            transactionLimitRepository.save(transactionLimit);

            // Update checking account with transaction limit reference and save again
            checkingAccount.setTransactionLimit(transactionLimit);
            accountRepository.save(checkingAccount);

            // Create savings account
            Account savingsAccount = new Account();
            savingsAccount.setIBAN(savingsIBAN);
            savingsAccount.setAccountType(AccountType.SAVINGS);
            savingsAccount.setBalance(0);
            savingsAccount.setActive(true);
            savingsAccount.setAbsoluteLimit(0);
            savingsAccount.setUser(user);

            // Save savings account
            accountRepository.save(savingsAccount);

            // Create transaction limit for savings account
            TransactionLimit transactionLimit2 = new TransactionLimit();
            transactionLimit2.setIBAN(savingsIBAN);
            transactionLimit2.setAccount(savingsAccount);
            transactionLimit2.setDailyLimit(transactionLimitDTO.getDailyLimit());
            transactionLimit2.setWeeklyLimit(transactionLimitDTO.getWeeklyLimit());
            transactionLimit2.setMonthlyLimit(transactionLimitDTO.getMonthlyLimit());

            // Save transaction limit for savings account
            transactionLimitRepository.save(transactionLimit2);

            // Update savings account with transaction limit reference and save again
            savingsAccount.setTransactionLimit(transactionLimit2);
            accountRepository.save(savingsAccount);
        }
    }

    private String generateUniqueIBAN(){
        String iban;
        do {
            iban = IBANGenerator.generateIBAN();
        } while (existsByIBAN(iban));
        return iban;
    }

    public boolean existsByIBAN(String iban) {
        // was is .ispresent() maar doet het niet meer???
        return accountRepository.findByIBAN(iban) != null;
    }

    //disable account
    public void disableAccount(Account account){
        account.setActive(false);
        accountRepository.save(account);
    }

    //enable account
    public void enableAccount(Account account){
        account.setActive(true);
        accountRepository.save(account);
    }


}
