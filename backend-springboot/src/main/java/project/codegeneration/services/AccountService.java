package project.codegeneration.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.codegeneration.models.*;
import project.codegeneration.models.DTO.ApproveUserDTO;
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
    public void createAccountForApprovedUser(User user, ApproveUserDTO approveUserDTO) {
        if (user.isApproved()) {
            createAccountWithLimits(user, approveUserDTO, AccountType.CHECKING);
            createAccountWithLimits(user, approveUserDTO, AccountType.SAVINGS);
        }
    }

    private void createAccountWithLimits(User user, ApproveUserDTO approveUserDTO, AccountType accountType) {
        String iban = generateUniqueIBAN();

        Account account = createAccount(user, approveUserDTO, iban, accountType);
        TransactionLimit transactionLimit = createTransactionLimit(approveUserDTO, iban);

        updateAccountWithTransactionLimit(account, transactionLimit);
    }

    private Account createAccount(User user, ApproveUserDTO approveUserDTO, String iban, AccountType accountType) {
        Account account = new Account();
        account.setIBAN(iban);
        account.setAccountType(accountType);
        account.setBalance(0);
        account.setActive(true);
        account.setAbsoluteLimit(approveUserDTO.getAbsoluteLimit());
        account.setUser(user);

        accountRepository.save(account);
        accountRepository.flush();

        return account;
    }

    private TransactionLimit createTransactionLimit(ApproveUserDTO approveUserDTO, String iban) {
        TransactionLimit transactionLimit = new TransactionLimit();
        transactionLimit.setIBAN(iban);
        transactionLimit.setDailyLimit(approveUserDTO.getTransactionLimit().getDailyLimit());
        transactionLimit.setWeeklyLimit(approveUserDTO.getTransactionLimit().getWeeklyLimit());
        transactionLimit.setMonthlyLimit(approveUserDTO.getTransactionLimit().getMonthlyLimit());

        transactionLimitRepository.save(transactionLimit);
        transactionLimitRepository.flush();

        return transactionLimit;
    }

    private void updateAccountWithTransactionLimit(Account account, TransactionLimit transactionLimit) {
        account.setTransactionLimit(transactionLimit);
        accountRepository.save(account);
        accountRepository.flush();
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

    public void disableAccount(Account account){
        account.setActive(false);
        accountRepository.save(account);
    }

    public void enableAccount(Account account){
        account.setActive(true);
        accountRepository.save(account);
    }

    public void setAbsoluteLimit(String iban, double limit) {
        Account account = accountRepository.findByIBAN(iban);
        account.setAbsoluteLimit(limit);
        accountRepository.save(account);
    }
}
