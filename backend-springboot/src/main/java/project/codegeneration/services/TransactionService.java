package project.codegeneration.services;

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

//    public void createATMTransaction(String sourceIBAN, String destinationIBAN, Double amount, TransactionType type, User user) {
//
//        Transaction transaction = new Transaction(sourceIBAN, destinationIBAN, amount, type, LocalDateTime.now(), user);
//        transactionRepository.save(transaction);
//        transactionRepository.flush();
//    }

    @Transactional
    public void createTransaction(String sourceIBAN, String destinationIBAN, Double amount, TransactionType type, User user) {

        Transaction transaction = new Transaction(sourceIBAN, destinationIBAN, amount, type, Instant.now().getEpochSecond(), user);
        transactionRepository.save(transaction);
        transactionRepository.flush();

        if (type == TransactionType.WITHDRAW || type == TransactionType.TRANSFER) {
            Account sourceAccount = accountService.getAccountByIBAN(sourceIBAN);

            if (user.getRoles().contains(Role.ROLE_ADMIN) ||  user.getId() == sourceAccount.getUser().getId()) {
                accountService.withdraw(sourceAccount, amount);
            } else {
                //ex
            }
        }

        if (type == TransactionType.DEPOSIT || type == TransactionType.TRANSFER) {
            Account destinationAccount = accountService.getAccountByIBAN(destinationIBAN);

            if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getId() == destinationAccount.getUser().getId()) {
                accountService.deposit(destinationAccount, amount);
            } else {
                //exx
            }
        }
    }
}
