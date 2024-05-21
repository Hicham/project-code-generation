package project.codegeneration.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.models.DTO.ATMTransactionRequest;
import project.codegeneration.models.Role;
import project.codegeneration.models.TransactionType;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionService;
import project.codegeneration.services.UserService;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(final AccountService accountService, UserService userService, TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts(@RequestParam(required = false) Integer userId, @RequestParam(required = false) Boolean isChecking) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));

        Optional<User> currentUser = userService.findByEmail(userDetails.getUsername());

        List<Account> accounts = null;

        // moet dit in de service?
        if (userId != null) {
            if (isAdmin || currentUser.isPresent() && currentUser.get().getId() == userId) {
                if (isChecking) {
                    accounts = accountService.getCheckingAccountsByUserId(userId);
                } else {
                    accounts = accountService.getAccountsByUserId(userId);
                }
            }
        } else {
            if (isAdmin) {
                accounts = accountService.getAllAccounts();
            }
        }
        if (accounts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<AccountDTO> accountDTOs = accounts.stream()
                .map(account -> new AccountDTO(account.getIBAN(), account.getUser().getId(), account.getAccountType().toString(), account.getBalance(), account.isActive(), account.getAbsoluteLimit()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDTOs);
    }


    @GetMapping("/accounts/{IBAN}")
    public ResponseEntity<AccountDTO> getAccountByIBAN(@PathVariable String IBAN, HttpServletRequest request) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Account account = accountService.getAccountByIBAN(IBAN);

        if (account == null) {
            return ResponseEntity.notFound().build();
        } else {
            AccountDTO accountDTO = new AccountDTO(account.getIBAN(), account.getUser().getId(), account.getAccountType().toString(), account.getBalance(), account.isActive(), account.getAbsoluteLimit());
            return ResponseEntity.ok(accountDTO);
        }
    }




    @PostMapping("/accounts/{IBAN}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String IBAN, @RequestBody ATMTransactionRequest ATMTransactionRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByEmail(currentUsername);

        if (currentUser.isPresent()) {
            transactionService.createTransaction(null, IBAN, ATMTransactionRequest.getAmount(), TransactionType.DEPOSIT, currentUser.get());
            return ResponseEntity.ok("Money deposited successfully.");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }


    }

    @PostMapping("/accounts/{IBAN}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String IBAN, @RequestBody ATMTransactionRequest ATMTransactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByEmail(currentUsername);

        if (currentUser.isPresent()) {
            transactionService.createTransaction(null, IBAN, ATMTransactionRequest.getAmount(), TransactionType.DEPOSIT, currentUser.get());
            return ResponseEntity.ok("Money withdrawn successfully.");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

    }


//    private boolean isAdmin(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return userDetails.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));
//    }

    public List<AccountDTO> getAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return accounts.stream().map(account -> new AccountDTO(account.getIBAN(), account.getUser(), account.getAccountType(), account.getBalance())).toList();
    }

    @GetMapping("/accounts/users/{email}")
    public AccountDTO getAccountsByUserEmail(@PathVariable String email) {
        Optional<Account> accounts = accountService.getAccountsByUserEmail(email);
        if (accounts.isPresent()) {
            Account account = accounts.get();
            return new AccountDTO(account.getIBAN(), account.getUser(), account.getAccountType(), account.getBalance());
        }
        else {
            return null;
        }

        }

}
