package project.codegeneration.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionLimitRepository transactionLimitRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccountByIBAN_ShouldReturnAccount_WhenAccountExists() {
        Account account = new Account();
        account.setIBAN("TESTIBAN");
        when(accountRepository.findByIBAN("TESTIBAN")).thenReturn(account);

        Account result = accountService.getAccountByIBAN("TESTIBAN");

        assertEquals("TESTIBAN", result.getIBAN());
        verify(accountRepository, times(1)).findByIBAN("TESTIBAN");
    }

    @Test
    void getAccountByIBAN_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountRepository.findByIBAN("TESTIBAN")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountByIBAN("TESTIBAN"));
        verify(accountRepository, times(1)).findByIBAN("TESTIBAN");
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts() {
        Account account1 = new Account();
        Account account2 = new Account();
        when(accountRepository.findAll()).thenReturn(List.of(account1, account2));

        List<Account> result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() {
        Account account = new Account();
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.updateAccount(account);

        assertEquals(account, result);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        Account account = new Account();
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.createAccount(account);

        assertEquals(account, result);
        verify(accountRepository, times(1)).save(account);
    }



    @Test
    void deposit_ShouldIncreaseBalance() {
        Account account = new Account();
        account.setBalance(100.0);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        accountService.deposit(account, 50.0);

        assertEquals(150.0, account.getBalance());
        verify(accountRepository, times(1)).saveAndFlush(account);
    }

    @Test
    void withdraw_ShouldDecreaseBalance() {
        Account account = new Account();
        account.setBalance(100.0);
        account.setAbsoluteLimit(0.0);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        accountService.withdraw(account, 50.0);

        assertEquals(50.0, account.getBalance());
        verify(accountRepository, times(1)).saveAndFlush(account);
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        Account account = new Account();
        account.setBalance(100.0);
        account.setAbsoluteLimit(0.0);

        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(account, 150.0));
        verify(accountRepository, never()).saveAndFlush(account);
    }

    @Test
    void getAccountsByUserId_ShouldReturnAccounts_WhenUserExists() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> accounts = new PageImpl<>(Collections.emptyList());
        when(accountRepository.findByUserId(pageable, userId)).thenReturn(accounts);

        Page<Account> result = accountService.getAccountsByUserId(pageable, userId);

        assertEquals(accounts, result);
        verify(accountRepository, times(1)).findByUserId(pageable, userId);
    }

    @Test
    void getAccountsByUserId_ShouldThrowException_WhenUserDoesNotExist() {
        int userId = 1;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountsByUserId(pageable, userId));
        verify(accountRepository, never()).findByUserId(pageable, userId);
    }

    @Test
    void isAccountOwner_ShouldReturnTrue_WhenEmailMatches() {
        String email = "test@example.com";
        String iban = "TESTIBAN";
        User user = new User();
        user.setEmail(email);
        Account account = new Account();
        account.setIBAN(iban);
        account.setUser(user);
        when(accountRepository.findByIBAN(iban)).thenReturn(account);

        boolean result = accountService.isAccountOwner(email, iban);

        assertTrue(result);
        verify(accountRepository, times(1)).findByIBAN(iban);
    }

    @Test
    void isAccountOwner_ShouldReturnFalse_WhenEmailDoesNotMatch() {
        String email = "test@example.com";
        String iban = "TESTIBAN";
        User user = new User();
        user.setEmail("different@example.com");
        Account account = new Account();
        account.setIBAN(iban);
        account.setUser(user);
        when(accountRepository.findByIBAN(iban)).thenReturn(account);

        boolean result = accountService.isAccountOwner(email, iban);

        assertFalse(result);
        verify(accountRepository, times(1)).findByIBAN(iban);
    }


}
