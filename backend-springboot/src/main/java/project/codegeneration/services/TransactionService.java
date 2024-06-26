package project.codegeneration.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.codegeneration.exceptions.CustomBadRequestException;
import project.codegeneration.exceptions.DailyTransactionLimitException;
import project.codegeneration.exceptions.ResourceNotFoundException;
import project.codegeneration.models.*;
import project.codegeneration.repositories.TransactionRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    @PersistenceContext
    private EntityManager entityManager;


    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void transferTransaction(String sourceIBAN, String destinationIBAN, Double amount, String description, TransactionType type, User user) {

        Account sourceAccount = accountService.getAccountByIBAN(sourceIBAN);
        Account destinationAccount = accountService.getAccountByIBAN(destinationIBAN);

        if (sourceAccount == null) {
            throw new ResourceNotFoundException("Cant find source account");
        }

        if (destinationAccount == null) {
            throw new ResourceNotFoundException("Cant find destination account");
        }

        double totalDailyTransactions = calculateTotalDailyTransactions(sourceIBAN);
        if (!user.getRoles().contains(Role.ROLE_ADMIN) && totalDailyTransactions + amount > sourceAccount.getTransactionLimit().getDailyLimit() && destinationAccount.getUser() != user) {
            throw new DailyTransactionLimitException();
        }


        if (user.getRoles().contains(Role.ROLE_ADMIN) || user.getId() == sourceAccount.getUser().getId()) {
            accountService.withdraw(sourceAccount, amount);
            accountService.deposit(destinationAccount, amount);

            Transaction transaction = new Transaction(sourceIBAN, destinationIBAN, amount, description, type, user);
            transactionRepository.saveAndFlush(transaction);
        } else {
            throw new AccessDeniedException("Not Authorized to perform this action.");
        }
    }

    @Transactional
    public void ATMTransaction(String sourceIBAN, String destinationIBAN, Double amount, String description, TransactionType type, User user) {


        if (type == TransactionType.WITHDRAW) {

            Account sourceAccount = accountService.getAccountByIBAN(sourceIBAN);

            double totalDailyTransactions = calculateTotalDailyTransactions(sourceIBAN);

            if (sourceAccount == null) {
                throw new ResourceNotFoundException("Cant find source account");
            }

            if (!user.getRoles().contains(Role.ROLE_ADMIN) && totalDailyTransactions + amount > sourceAccount.getTransactionLimit().getDailyLimit()) {
                throw new DailyTransactionLimitException();
            }


            accountService.withdraw(sourceAccount, amount);

        }

        if (type == TransactionType.DEPOSIT) {
            Account destinationAccount = accountService.getAccountByIBAN(destinationIBAN);

            if (destinationAccount == null) {
                throw new ResourceNotFoundException("Cant find destination account");
            }

            accountService.deposit(destinationAccount, amount);

        }
        Transaction transaction = new Transaction(sourceIBAN, destinationIBAN, amount, description, type, user);
        transactionRepository.saveAndFlush(transaction);
    }

    public double calculateTotalDailyTransactions(String iban) {
        LocalDate today = LocalDate.now();
        Long startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long endOfDay = today.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();


        Optional<Double> sum = transactionRepository.findSumBySourceIBANAndTimestampBetweenExcludingOwnTransfers(
                iban,
                startOfDay,
                endOfDay
        );


       return sum.orElse(0.0);

    }

    public Page<Transaction> getTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);

    }

    public Page<Transaction> getAccountTransactions(String ownIban, LocalDate startDate, LocalDate endDate, Double amount, String amountCondition, String ibanFilter, String ibanType, Pageable pageable) {
        if (accountService.getAccountByIBAN(ownIban) == null) {
            throw new ResourceNotFoundException("IBAN not found");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> transaction = query.from(Transaction.class);

        List<Predicate> predicates = createPredicates(cb, transaction, ownIban, startDate, endDate, amount, amountCondition, ibanFilter, ibanType);

        query.where(predicates.toArray(new Predicate[0]));

        CriteriaQuery<Transaction> select = query.select(transaction);

        return executePagedQuery(select, pageable);
    }

    private List<Predicate> createPredicates(CriteriaBuilder cb, Root<Transaction> transaction, String ownIban, LocalDate startDate, LocalDate endDate, Double amount, String amountCondition, String ibanFilter, String ibanType) {
        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(transaction.get("timestamp"), convertDateToTimestamp(startDate)));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(transaction.get("timestamp"), convertDateToTimestamp(endDate)));
        }

        if (amount != null && amountCondition != null) {
            switch (amountCondition) {
                case "greaterThan":
                    predicates.add(cb.greaterThan(transaction.get("amount"), amount));
                    break;
                case "lessThan":
                    predicates.add(cb.lessThan(transaction.get("amount"), amount));
                    break;
                case "equalTo":
                    predicates.add(cb.equal(transaction.get("amount"), amount));
                    break;
                default:
                    throw new CustomBadRequestException("Invalid amountCondition: " + amountCondition);
            }
        }

        Predicate ownIbanPredicate = cb.or(
                cb.equal(transaction.get("sourceIBAN"), ownIban),
                cb.equal(transaction.get("destinationIBAN"), ownIban)
        );
        predicates.add(ownIbanPredicate);

        if (ibanFilter != null && ibanType != null) {
            switch (ibanType) {
                case "source":
                    predicates.add(cb.equal(transaction.get("sourceIBAN"), ibanFilter));
                    break;
                case "destination":
                    predicates.add(cb.equal(transaction.get("destinationIBAN"), ibanFilter));
                    break;
                default:
                    throw new CustomBadRequestException("Invalid ibanType: " + ibanType);
            }
        }

        return predicates;
    }

    private Page<Transaction> executePagedQuery(CriteriaQuery<Transaction> query, Pageable pageable) {
        TypedQuery<Transaction> typedQuery = entityManager.createQuery(query);

        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Transaction> transactionList = typedQuery.getResultList();

        return new PageImpl<>(transactionList, pageable, totalRows);
    }



    public static long convertDateToTimestamp(LocalDate date) {

        try {
            Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
            return instant.toEpochMilli();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't parse date");
        }

    }

    //    public static long convertDateToTimestamp(String dateString) {
//
//        try {
//            LocalDate localDate = LocalDate.parse(dateString);
//
//            Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
//
//            return instant.toEpochMilli();
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Can't parse date");
//        }
//
//    }

}
