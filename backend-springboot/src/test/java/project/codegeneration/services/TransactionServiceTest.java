package project.codegeneration.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import project.codegeneration.exceptions.ResourceNotFoundException;
import project.codegeneration.models.*;
import project.codegeneration.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TransactionService transactionService;

    private Account sourceAccount;
    private Account destinationAccount;
    private User user;

    @BeforeEach
    public void setup() {
        user = new User(List.of(Role.ROLE_ADMIN), false, "wouter123@gmail.com", "test", "test", "test", "3652574", "06352615");
        user.setId(1);

        sourceAccount = new Account();
        sourceAccount.setIBAN("SOURCE_IBAN");
        sourceAccount.setTransactionLimit(new TransactionLimit("SOURCE_IBAN", 500.00));
        sourceAccount.setUser(user);

        destinationAccount = new Account();
        destinationAccount.setIBAN("DEST_IBAN");

        when(accountService.getAccountByIBAN("SOURCE_IBAN")).thenReturn(sourceAccount);
        when(accountService.getAccountByIBAN("DEST_IBAN")).thenReturn(destinationAccount);
    }

    @Test
    public void testTransferTransaction_Success() {
        double amount = 100.0;

        when(transactionRepository.findSumBySourceIBANAndTimestampBetween(anyString(), anyLong(), anyLong()))
                .thenReturn(0.0);

        transactionService.transferTransaction("SOURCE_IBAN", "DEST_IBAN", amount, "Test Transfer", TransactionType.TRANSFER, user);

        verify(accountService).withdraw(sourceAccount, amount);
        verify(accountService).deposit(destinationAccount, amount);
        verify(transactionRepository).saveAndFlush(any(Transaction.class));
    }

    @Test
    public void testTransferTransaction_DailyLimitExceeded() {
        double amount = 6000.0;

        when(transactionRepository.findSumBySourceIBANAndTimestampBetween(anyString(), anyLong(), anyLong()))
                .thenReturn(0.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transferTransaction("SOURCE_IBAN", "DEST_IBAN", amount, "Test Transfer", TransactionType.TRANSFER, user);
        });

        assertEquals("Daily transaction limit exceeded.", exception.getMessage());
    }

    @Test
    public void testTransferTransaction_SourceAccountNotFound() {
        when(accountService.getAccountByIBAN("SOURCE_IBAN")).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transferTransaction("SOURCE_IBAN", "DEST_IBAN", 100.0, "Test Transfer", TransactionType.TRANSFER, user);
        });

        assertEquals("Cant find source account", exception.getMessage());
    }

    @Test
    public void testTransferTransaction_UnauthorizedUser() {
        User otherUser = new User();
        otherUser.setId(2);
        sourceAccount.setUser(otherUser);

        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            transactionService.transferTransaction("SOURCE_IBAN", "DEST_IBAN", 100.0, "Test Transfer", TransactionType.TRANSFER, user);
        });

        assertEquals("Not Authorized to perform this action.", exception.getMessage());
    }

    @Test
    public void testGetAccountTransactions() {
        Pageable pageable = PageRequest.of(0, 10);
        Transaction transaction = new Transaction("SOURCE_IBAN", "DEST_IBAN", 100.0, "Test Transaction", TransactionType.TRANSFER, user);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Page<Transaction> page = new PageImpl<>(transactions, pageable, 1);

        // Mock the EntityManager and related objects
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<Transaction> criteriaQuery = mock(CriteriaQuery.class);
        Root<Transaction> root = mock(Root.class);
        TypedQuery<Transaction> typedQuery = mock(TypedQuery.class);

        // Setup mock behavior
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Transaction.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Transaction.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(transactions);

        // Call the service method
        Page<Transaction> result = transactionService.getAccountTransactions("SOURCE_IBAN", null, null, null, null, null, null, pageable);

        // Assertions
        assertEquals(page.getTotalElements(), result.getTotalElements());
        assertEquals(page.getContent().size(), result.getContent().size());
        verify(entityManager).createQuery(criteriaQuery);
    }
}
