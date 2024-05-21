package project.codegeneration.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;
import project.codegeneration.models.TransactionType;
import project.codegeneration.models.User;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.util.IBANGenerator;

import java.util.List;
import java.util.Optional;

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


    public Optional<Account> getAccountsByUserEmail(String email) {
        return accountRepository.findAccountsByUserEmail(email);
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
    public void createAccountForApprovedUser(User user){
        if(user.isApproved()){
            //Generate unique IBAN
            String checkingIBAN = generateUniqueIBAN();
            String savingsIBAN = generateUniqueIBAN();

            //Create checking account
            Account checkingAccount = new Account();
            checkingAccount.setIBAN(checkingIBAN);
            checkingAccount.setAccountType(AccountType.CHECKING);
            checkingAccount.setBalance(0);

            checkingAccount.setActive(true);
            checkingAccount.setAbsoluteLimit(1000);
            checkingAccount.setUser(user);

            //Create savings account
            Account savingsAccount = new Account();
            savingsAccount.setIBAN(savingsIBAN);
            savingsAccount.setAccountType(AccountType.SAVINGS);
            savingsAccount.setBalance(0);
            savingsAccount.setActive(true);
            savingsAccount.setAbsoluteLimit(1000);
            savingsAccount.setUser(user);

            //Save Accounts
            accountRepository.save(checkingAccount);
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

    public int getRandomPinCode(){
        return (int) (Math.random() * 10000);
    }

}
