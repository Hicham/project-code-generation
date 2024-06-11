package project.codegeneration.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import project.codegeneration.exceptions.DailyTransactionLimitException;
import project.codegeneration.exceptions.ResourceNotFoundException;
import project.codegeneration.models.*;
import project.codegeneration.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private EntityManager entityManager;


    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Transaction> criteriaQuery;

    @Mock
    private Root<Transaction> root;

    @Mock
    private TypedQuery<Transaction> typedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferTransaction_Success() {
        User user = new User();
        user.setId(1);
        user.setRoles(Arrays.asList(Role.ROLE_USER));

        TransactionLimit transactionLimit = new TransactionLimit("sourceIBAN", 5000.0);

        Account sourceAccount = new Account();
        sourceAccount.setIBAN("sourceIBAN");
        sourceAccount.setBalance(1000.0);
        sourceAccount.setUser(user);
        sourceAccount.setTransactionLimit(transactionLimit);

        Account destinationAccount = new Account();
        destinationAccount.setIBAN("destinationIBAN");
        destinationAccount.setBalance(500.0);

        when(accountService.getAccountByIBAN("sourceIBAN")).thenReturn(sourceAccount);
        when(accountService.getAccountByIBAN("destinationIBAN")).thenReturn(destinationAccount);
        when(transactionRepository.findSumBySourceIBANAndTimestampBetweenExcludingOwnTransfers(anyString(), anyLong(), anyLong())).thenReturn(Optional.of(0.0));

        transactionService.transferTransaction("sourceIBAN", "destinationIBAN", 200.0, "Test transfer", TransactionType.TRANSFER, user);

        verify(accountService).withdraw(sourceAccount, 200.0);
        verify(accountService).deposit(destinationAccount, 200.0);
        verify(transactionRepository).saveAndFlush(any(Transaction.class));
    }

    @Test
    void testTransferTransaction_SourceAccountNotFound() {
        User user = new User();
        user.setId(1);

        when(accountService.getAccountByIBAN("sourceIBAN")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.transferTransaction("sourceIBAN", "destinationIBAN", 200.0, "Test transfer", TransactionType.TRANSFER, user));
    }

    @Test
    void testTransferTransaction_DestinationAccountNotFound() {
        User user = new User();
        user.setId(1);

        Account sourceAccount = new Account();
        sourceAccount.setIBAN("sourceIBAN");

        when(accountService.getAccountByIBAN("sourceIBAN")).thenReturn(sourceAccount);
        when(accountService.getAccountByIBAN("destinationIBAN")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.transferTransaction("sourceIBAN", "destinationIBAN", 200.0, "Test transfer", TransactionType.TRANSFER, user));
    }

    @Test
    void testTransferTransaction_DailyLimitExceeded() {
        User user = new User();
        user.setId(1);

        TransactionLimit transactionLimit = new TransactionLimit("sourceIBAN", 1000.0);

        Account sourceAccount = new Account();
        sourceAccount.setIBAN("sourceIBAN");
        sourceAccount.setTransactionLimit(transactionLimit);

        Account destinationAccount = new Account();
        destinationAccount.setIBAN("destinationIBAN");

        when(accountService.getAccountByIBAN("sourceIBAN")).thenReturn(sourceAccount);
        when(accountService.getAccountByIBAN("destinationIBAN")).thenReturn(destinationAccount);
        when(transactionRepository.findSumBySourceIBANAndTimestampBetweenExcludingOwnTransfers(anyString(), anyLong(), anyLong())).thenReturn(Optional.of(900.0));

        assertThrows(DailyTransactionLimitException.class, () -> transactionService.transferTransaction("sourceIBAN", "destinationIBAN", 200.0, "Test transfer", TransactionType.TRANSFER, user));
    }

    @Test
    void testATMTransaction_Deposit() {
        User user = new User();
        user.setId(1);

        Account destinationAccount = new Account();
        destinationAccount.setIBAN("destinationIBAN");

        when(accountService.getAccountByIBAN("destinationIBAN")).thenReturn(destinationAccount);

        transactionService.ATMTransaction(null, "destinationIBAN", 200.0, "ATM deposit", TransactionType.DEPOSIT, user);

        verify(accountService).deposit(destinationAccount, 200.0);
        verify(transactionRepository).saveAndFlush(any(Transaction.class));
    }

    @Test
    void testATMTransaction_Withdraw() {
        User user = new User();
        user.setId(1);

        TransactionLimit transactionLimit = new TransactionLimit("sourceIBAN", 1000.0);

        Account sourceAccount = new Account();
        sourceAccount.setIBAN("sourceIBAN");
        sourceAccount.setTransactionLimit(transactionLimit);

        when(accountService.getAccountByIBAN("sourceIBAN")).thenReturn(sourceAccount);
        when(transactionRepository.findSumBySourceIBANAndTimestampBetweenExcludingOwnTransfers(anyString(), anyLong(), anyLong())).thenReturn(Optional.of(500.0));

        transactionService.ATMTransaction("sourceIBAN", null, 200.0, "ATM withdraw", TransactionType.WITHDRAW, user);

        verify(accountService).withdraw(sourceAccount, 200.0);
        verify(transactionRepository).saveAndFlush(any(Transaction.class));
    }

    @Test
    void testATMTransaction_DestinationAccountNotFound() {
        User user = new User();
        user.setId(1);

        when(accountService.getAccountByIBAN("destinationIBAN")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.ATMTransaction(null, "destinationIBAN", 200.0, "ATM deposit", TransactionType.DEPOSIT, user));
    }

    @Test
    void testATMTransaction_SourceAccountNotFound() {
        User user = new User();
        user.setId(1);

        when(accountService.getAccountByIBAN("sourceIBAN")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.ATMTransaction("sourceIBAN", null, 200.0, "ATM withdraw", TransactionType.WITHDRAW, user));
    }


}
