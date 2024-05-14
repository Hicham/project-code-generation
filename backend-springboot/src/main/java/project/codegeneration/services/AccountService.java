package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.Account;
import project.codegeneration.models.User;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.util.IBANGenerator;

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

    public void createAccountForApprovedUser(User user){
        if(user.isApproved()){
            //Generate unique IBAN
            String checkingIBAN = generateUniqueIBAN();
            String savingsIBAN = generateUniqueIBAN();

            //Create checking account
            Account checkingAccount = new Account();
            checkingAccount.setIBAN(checkingIBAN);
            checkingAccount.setAccountType("Checking");
            checkingAccount.setBalance(0);
            checkingAccount.setPinCode(getRandomPinCode());
            checkingAccount.setActive(true);
            checkingAccount.setAbsoluteLimit(1000);
            checkingAccount.setUserId(user);

            //Create savings account
            Account savingsAccount = new Account();
            savingsAccount.setIBAN(savingsIBAN);
            savingsAccount.setAccountType("Savings");
            savingsAccount.setBalance(0);
            savingsAccount.setPinCode(getRandomPinCode());
            savingsAccount.setActive(true);
            savingsAccount.setAbsoluteLimit(1000);
            savingsAccount.setUserId(user);

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
        return accountRepository.findByIBAN(iban).isPresent();
    }

    public int getRandomPinCode(){
        return (int) (Math.random() * 10000);
    }
}
