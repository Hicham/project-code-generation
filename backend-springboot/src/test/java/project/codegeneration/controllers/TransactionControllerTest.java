package project.codegeneration.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import project.codegeneration.models.DTO.ATMTransactionRequest;
import project.codegeneration.models.DTO.TransactionRequestDTO;
import project.codegeneration.models.Transaction;
import project.codegeneration.models.TransactionType;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionService;
import project.codegeneration.services.UserService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetTransactions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> transactions = new PageImpl<>(Arrays.asList(new Transaction(), new Transaction()));
        when(transactionService.getTransactions(pageable)).thenReturn(transactions);

        ResponseEntity<?> response = transactionController.getTransactions(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testGetAccountTransactions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> transactions = new PageImpl<>(Arrays.asList(new Transaction(), new Transaction()));
        when(transactionService.getAccountTransactions(anyString(), anyString(), anyString(), anyDouble(), anyString(), anyString(), anyString(), eq(pageable))).thenReturn(transactions);

        ResponseEntity<?> response = transactionController.getAccountTransactions("IBAN123", 0, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    void testCreateTransaction() {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO("IBAN123", "IBAN123", 500.00, "description" );
        transactionRequest.setAmount(100.0);
        transactionRequest.setSourceIBAN("sourceIBAN");
        transactionRequest.setDestinationIBAN("destinationIBAN");

        User user = new User();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        ResponseEntity<?> response = transactionController.createTransaction(transactionRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(transactionService, times(1)).transferTransaction(anyString(), anyString(), anyDouble(), anyString(), any(TransactionType.class), any(User.class));
    }

    @Test
    void testDeposit() {
        ATMTransactionRequest atmTransactionRequest = new ATMTransactionRequest();
        atmTransactionRequest.setAmount(100.0);

        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user).get());
        when(accountService.isAccountOwner(anyString(), anyString())).thenReturn(true);
        doNothing().when(transactionService).ATMTransaction(any(), anyString(), anyDouble(), any(), any(), any());

        ResponseEntity<String> response = transactionController.deposit("IBAN123", atmTransactionRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Money deposited successfully.", response.getBody());
        verify(transactionService, times(1)).ATMTransaction(null, "IBAN123", 100.0, null, TransactionType.DEPOSIT, user);
    }

    @Test
    void testWithdraw() {
        ATMTransactionRequest atmTransactionRequest = new ATMTransactionRequest();
        atmTransactionRequest.setAmount(100.0);

        User user = new User();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("limit@gmail.com");
        when(userService.getUserByEmail("limit@gmail.com")).thenReturn(user);
        when(accountService.isAccountOwner(anyString(), anyString())).thenReturn(true);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(user).get());

        ResponseEntity<String> response = transactionController.withdraw("NL55INHO2675092764", atmTransactionRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Money deposited successfully.", response.getBody());
        verify(transactionService, times(1)).ATMTransaction("NL55INHO2675092764", null, 100.0, null, TransactionType.WITHDRAW, user);
    }
}
