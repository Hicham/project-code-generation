package project.codegeneration.services;

import jakarta.security.auth.message.AuthException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.codegeneration.models.*;
import project.codegeneration.repositories.TransactionRepository;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void transferTransaction(String sourceIBAN, String destinationIBAN, Double amount, String description, TransactionType type, User user)
    {
        Transaction transaction = new Transaction(sourceIBAN, destinationIBAN, amount, description, type, user);
        transactionRepository.saveAndFlush(transaction);

        Account sourceAccount = accountService.getAccountByIBAN(sourceIBAN);
        Account destinationAccount = accountService.getAccountByIBAN(destinationIBAN);

        if (user.getRoles().contains(Role.ROLE_ADMIN) ||  user.getId() == sourceAccount.getUser().getId()) {
            accountService.withdraw(sourceAccount, amount);
            accountService.deposit(destinationAccount, amount);
        } else {
            throw new AccessDeniedException("Not Authorized to perform this action.");
        }
    }

    @Transactional
    public void ATMTransaction(String sourceIBAN, String destinationIBAN, Double amount, String description, TransactionType type, User user) {

        Transaction transaction = new Transaction(sourceIBAN, destinationIBAN, amount, description, type, user);
        transactionRepository.saveAndFlush(transaction);

        if (type == TransactionType.WITHDRAW) {
            Account sourceAccount = accountService.getAccountByIBAN(sourceIBAN);

            if (user.getRoles().contains(Role.ROLE_ADMIN) ||  user.getId() == sourceAccount.getUser().getId()) {
                accountService.withdraw(sourceAccount, amount);
            } else {
                throw new AccessDeniedException("Not Authorized to perform this action.");
            }
        }

        if (type == TransactionType.DEPOSIT) {
            Account destinationAccount = accountService.getAccountByIBAN(destinationIBAN);

            if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getId() == destinationAccount.getUser().getId()) {
                accountService.deposit(destinationAccount, amount);
            } else {
                throw new AccessDeniedException("Not Authorized to perform this action.");
            }
        }
    }






    public Page<Transaction> getTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);

    }

    public Page<Transaction> getAccountTransactions(String iban, Pageable pageable) {
        return transactionRepository.findBySourceIBANOrDestinationIBANOrderByTimestampDesc(iban, iban, pageable);
    }

}
