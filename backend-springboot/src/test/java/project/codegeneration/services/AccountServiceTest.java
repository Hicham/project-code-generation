package project.codegeneration.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.codegeneration.exceptions.InsufficientFundsException;
import project.codegeneration.exceptions.ResourceNotFoundException;
import project.codegeneration.models.*;
import project.codegeneration.models.DTO.ApproveUserDTO;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.models.DTO.TransactionRequestDTO;
import project.codegeneration.repositories.AccountRepository;
import project.codegeneration.repositories.TransactionLimitRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionLimitRepository transactionLimitRepository;

    @Mock
    private UserService userService;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountById() {
        Account account = new Account();
        account.setIBAN("testIBAN");

        when(accountRepository.findById(anyInt())).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(1);
        assertEquals("testIBAN", result.getIBAN());
    }

    @Test
    void testGetAccountByIBAN_Success() {
        Account account = new Account();
        account.setIBAN("testIBAN");

        when(accountRepository.findByIBAN("testIBAN")).thenReturn(account);

        Account result = accountService.getAccountByIBAN("testIBAN");
        assertEquals("testIBAN", result.getIBAN());
    }

    @Test
    void testGetAccountByIBAN_AccountNotFound() {
        when(accountRepository.findByIBAN("testIBAN")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountByIBAN("testIBAN"));
    }

    @Test
    void testGetAllAccounts_Pageable() {
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        Page<Account> page = new PageImpl<>(accounts);

        when(accountRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Account> result = accountService.getAllAccounts(PageRequest.of(0, 10));
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = Arrays.asList(new Account(), new Account());

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateAccount() {
        Account account = new Account();
        account.setIBAN("testIBAN");

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.updateAccount(account);
        assertEquals("testIBAN", result.getIBAN());
    }

    @Test
    void testCreateAccount() {
        Account account = new Account();
        account.setIBAN("testIBAN");

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.createAccount(account);
        assertEquals("testIBAN", result.getIBAN());
    }

    @Test
    void testDeleteAccount() {
        accountService.deleteAccount(1);
        verify(accountRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAccountsByUserId_Success() {
        User user = new User();
        user.setId(1);

        List<Account> accounts = Arrays.asList(new Account(), new Account());
        Page<Account> page = new PageImpl<>(accounts);

        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(any(Pageable.class), anyInt())).thenReturn(page);

        Page<Account> result = accountService.getAccountsByUserId(PageRequest.of(0, 10), 1);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetAccountsByUserId_UserNotFound() {
        when(userService.getUserById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountsByUserId(PageRequest.of(0, 10), 1));
    }

    @Test
    void testGetAccountsByUserEmail() {
        Account account = new Account();
        account.setIBAN("testIBAN");

        when(accountRepository.findAccountsByUserEmail(anyString())).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountsByUserEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("testIBAN", result.get().getIBAN());
    }

    @Test
    void testGetCheckingAccountsByUserId_Success() {
        User user = new User();
        user.setId(1);

        List<Account> accounts = Arrays.asList(new Account(), new Account());
        Page<Account> page = new PageImpl<>(accounts);

        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));
        when(accountRepository.findByUserIdAndAccountType(any(Pageable.class), anyInt(), any(AccountType.class))).thenReturn(page);

        Page<Account> result = accountService.getCheckingAccountsByUserId(PageRequest.of(0, 10), 1);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetCheckingAccountsByUserId_UserNotFound() {
        when(userService.getUserById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getCheckingAccountsByUserId(PageRequest.of(0, 10), 1));
    }

    @Test
    void testDeposit() {
        Account account = new Account();
        account.setBalance(100.0);

        accountService.deposit(account, 50.0);
        assertEquals(150.0, account.getBalance());
        verify(accountRepository, times(1)).saveAndFlush(account);
    }

    @Test
    void testDeposit_NegativeAmount() {
        Account account = new Account();
        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(account, -50.0));
    }

    @Test
    void testWithdraw() {
        Account account = new Account();
        account.setBalance(100.0);
        account.setAbsoluteLimit(-50.0);

        accountService.withdraw(account, 50.0);
        assertEquals(50.0, account.getBalance());
        verify(accountRepository, times(1)).saveAndFlush(account);
    }

    @Test
    void testWithdraw_NegativeAmount() {
        Account account = new Account();
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(account, -50.0));
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        Account account = new Account();
        account.setBalance(100.0);
        account.setAbsoluteLimit(0);

        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(account, 150.0));
    }


    @Test
    void testDisableAccount() {
        Account account = new Account();
        account.setActive(true);

        accountService.disableAccount(account);
        assertFalse(account.isActive());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testEnableAccount() {
        Account account = new Account();
        account.setActive(false);

        accountService.enableAccount(account);
        assertTrue(account.isActive());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testSetAbsoluteLimit() {
        Account account = new Account();
        account.setIBAN("testIBAN");

        when(accountRepository.findByIBAN("testIBAN")).thenReturn(account);

        accountService.setAbsoluteLimit("testIBAN", 500.0);
        assertEquals(500.0, account.getAbsoluteLimit());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testIsAccountOwner() {
        User user = new User();
        user.setEmail("test@example.com");

        Account account = new Account();
        account.setIBAN("testIBAN");
        account.setUser(user);

        when(accountRepository.findByIBAN("testIBAN")).thenReturn(account);

        boolean result = accountService.isAccountOwner("test@example.com", "testIBAN");
        assertTrue(result);
    }


}
