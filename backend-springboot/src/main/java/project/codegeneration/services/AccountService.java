package project.codegeneration.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.codegeneration.exceptions.InsufficientFundsException;
import project.codegeneration.exceptions.ResourceNotFoundException;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;
import project.codegeneration.models.DTO.ApproveUserDTO;
import project.codegeneration.models.TransactionLimit;
import project.codegeneration.models.User;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.TransactionLimitRepository;
import project.codegeneration.util.IBANGenerator;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final TransactionLimitRepository transactionLimitRepository;

    private final UserService userService;

    public AccountService(AccountRepository accountRepository, TransactionLimitRepository transactionLimitRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.transactionLimitRepository = transactionLimitRepository;
        this.userService = userService;

    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElseThrow();
    }

    public Account getAccountByIBAN(String IBAN) {

        Account account = accountRepository.findByIBAN(IBAN);

        if(account == null)
        {
            throw new ResourceNotFoundException("Account not found");
        }

        return account;
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

        if (userService.getUserById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

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
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.saveAndFlush(account);
    }


    public void withdraw(Account account, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        if (account.getBalance() - amount < account.getAbsoluteLimit()) {
           throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.saveAndFlush(account);
    }

    public void createAccountForApprovedUser(User user, ApproveUserDTO approveUserDTO) {
        if (user.isApproved()) {
            String checkingIBAN = generateUniqueIBAN();
            String savingsIBAN = generateUniqueIBAN();

            Account checkingAccount = createAccount(user, checkingIBAN, AccountType.CHECKING, approveUserDTO.getAbsoluteLimit());
            Account savingsAccount = createAccount(user, savingsIBAN, AccountType.SAVINGS, approveUserDTO.getAbsoluteLimit());

            createAndAssignTransactionLimit(checkingAccount, approveUserDTO.getTransactionLimit().getDailyLimit());
            createAndAssignTransactionLimit(savingsAccount, approveUserDTO.getTransactionLimit().getDailyLimit());
        }
    }

    private Account createAccount(User user, String IBAN, AccountType accountType, double absoluteLimit) {
        Account account = new Account();
        account.setIBAN(IBAN);
        account.setAccountType(accountType);
        account.setBalance(0);
        account.setActive(true);
        account.setAbsoluteLimit(absoluteLimit);
        account.setUser(user);
        accountRepository.save(account);
        return account;
    }

    private void createAndAssignTransactionLimit(Account account, double dailyLimit) {
        TransactionLimit transactionLimit = new TransactionLimit();
        transactionLimit.setIBAN(account.getIBAN());
        transactionLimit.setAccount(account);
        transactionLimit.setDailyLimit(dailyLimit);
        transactionLimitRepository.save(transactionLimit);

        account.setTransactionLimit(transactionLimit);
        accountRepository.save(account);
    }


    private String generateUniqueIBAN() {
        String iban;
        do {
            iban = IBANGenerator.generateUniqueIBAN();
        } while (existsByIBAN(iban));
        return iban;
    }

    public boolean existsByIBAN(String iban) {
        // was is .ispresent() maar doet het niet meer???
        return accountRepository.findByIBAN(iban) != null;
    }

    public void disableAccount(Account account) {
        account.setActive(false);
        accountRepository.save(account);
    }

    public void enableAccount(Account account) {
        account.setActive(true);
        accountRepository.save(account);
    }

    public void setAbsoluteLimit(String iban, double limit) {
        Account account = accountRepository.findByIBAN(iban);
        account.setAbsoluteLimit(limit);
        accountRepository.save(account);
    }

    public boolean isAccountOwner(String email, String IBAN) {
        Account account = getAccountByIBAN(IBAN);
        return account != null && account.getUser().getEmail().equals(email);
    }
}
