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
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionLimitService;
import project.codegeneration.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @Mock
    private TransactionLimitService transactionLimitService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountsChecking() {
        Integer userId = 1;
        Account account = new Account();
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountService.getCheckingAccountsByUserId(any(Pageable.class), eq(userId))).thenReturn(accountPage);

        ResponseEntity<?> responseEntity = accountController.getAccountsChecking(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Page<AccountDTO> accountDTOPage = (Page<AccountDTO>) responseEntity.getBody();
        assertEquals(1, accountDTOPage.getTotalElements());
        verify(accountService, times(1)).getCheckingAccountsByUserId(any(Pageable.class), eq(userId));
    }

    @Test
    void testGetAccountsByUser() {
        Integer userId = 1;
        Integer pageNumber = 0;
        Account account = new Account();
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountService.getAccountsByUserId(any(Pageable.class), eq(userId))).thenReturn(accountPage);

        ResponseEntity<?> responseEntity = accountController.getAccountsByUser(pageNumber, userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Page<AccountDTO> accountDTOPage = (Page<AccountDTO>) responseEntity.getBody();
        assertEquals(1, accountDTOPage.getTotalElements());
        verify(accountService, times(1)).getAccountsByUserId(any(Pageable.class), eq(userId));
    }

    @Test
    void testGetAccounts() {
        Integer pageNumber = 0;
        Account account = new Account();
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountService.getAllAccounts(any(Pageable.class))).thenReturn(accountPage);

        ResponseEntity<?> responseEntity = accountController.getAccounts(pageNumber);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Page<AccountDTO> accountDTOPage = (Page<AccountDTO>) responseEntity.getBody();
        assertEquals(1, accountDTOPage.getTotalElements());
        verify(accountService, times(1)).getAllAccounts(any(Pageable.class));
    }

    @Test
    void testGetAccountByIBAN() {
        String IBAN = "123456";
        Account account = new Account();
        when(accountService.getAccountByIBAN(IBAN)).thenReturn(account);

        ResponseEntity<AccountDTO> responseEntity = accountController.getAccountByIBAN(IBAN);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AccountDTO accountDTO = responseEntity.getBody();
        assertEquals(account.getIBAN(), accountDTO.getIBAN());
        verify(accountService, times(1)).getAccountByIBAN(IBAN);
    }

    @Test
    void testGetAccountByIBAN_NotFound() {
        String IBAN = "123456";
        when(accountService.getAccountByIBAN(IBAN)).thenReturn(null);

        ResponseEntity<AccountDTO> responseEntity = accountController.getAccountByIBAN(IBAN);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(accountService, times(1)).getAccountByIBAN(IBAN);
    }

    @Test
    void testDisableAccount() {
        String IBAN = "123456";
        Account account = new Account();
        account.setActive(true);
        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(accountService.getAccountByIBAN(IBAN)).thenReturn(account);

        ResponseEntity<String> responseEntity = accountController.disableAccount(IBAN);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Account disabled successfully.", responseEntity.getBody());
        verify(accountService, times(1)).getAccountByIBAN(IBAN);
        verify(accountService, times(1)).updateAccount(any(Account.class));
    }

    @Test
    void testEnableAccount() {
        String IBAN = "123456";
        Account account = new Account();
        account.setActive(false);
        User user = new User();
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(accountService.getAccountByIBAN(IBAN)).thenReturn(account);

        ResponseEntity<String> responseEntity = accountController.enableAccount(IBAN);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Account enabled successfully.", responseEntity.getBody());
        verify(accountService, times(1)).getAccountByIBAN(IBAN);
        verify(accountService, times(1)).updateAccount(any(Account.class));
    }

    @Test
    void testSetTransactionLimits() {
        String iban = "123456";
        TransactionLimitDTO transactionLimitDTO = new TransactionLimitDTO("NL38INHO3001470910", 500.00, 0);
        transactionLimitDTO.setAbsoluteLimit(1000);
        transactionLimitDTO.setDailyLimit(500);

        ResponseEntity<String> responseEntity = accountController.setTransactionLimits(iban, transactionLimitDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Transaction limits updated successfully", responseEntity.getBody());
        verify(accountService, times(1)).setAbsoluteLimit(iban, transactionLimitDTO.getAbsoluteLimit());
        verify(transactionLimitService, times(1)).setTransactionLimit(iban, transactionLimitDTO.getDailyLimit());
    }

    @Test
    void testGetAllAccounts() {
        Account account = new Account();
        when(accountService.getAllAccounts()).thenReturn(Collections.singletonList(account));

        ResponseEntity<List<AccountDTO>> responseEntity = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<AccountDTO> accountDTOs = responseEntity.getBody();
        assertEquals(1, accountDTOs.size());
        verify(accountService, times(1)).getAllAccounts();
    }
}
