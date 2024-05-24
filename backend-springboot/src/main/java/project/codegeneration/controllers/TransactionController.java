package project.codegeneration.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.*;
import project.codegeneration.models.DTO.ATMTransactionRequest;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionService;
import project.codegeneration.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {


    private final TransactionService transactionService;
    private final AccountService accountService;


    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }


    @GetMapping("/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(@RequestParam(required = false, defaultValue = "0") Integer pageNumber) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));

            if (isAdmin)
            {
                Pageable pageable = PageRequest.of(pageNumber, 10);
                return ResponseEntity.ok(transactionService.getTransactions(pageable));
            }
            else
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/accounts/{iban}/transactions")
    public ResponseEntity<Page<Transaction>> getAccountTransactions(@PathVariable String iban,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer pageNumber) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Optional<Account> optionalAccount = Optional.ofNullable(accountService.getAccountByIBAN(iban));
            if (optionalAccount.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));

            if (isAdmin || userDetails.getUsername().equals(optionalAccount.get().getUser().getEmail())) {
                Pageable pageable = PageRequest.of(pageNumber, 10);
                Page<Transaction> transactions = transactionService.getAccountTransactions(iban, pageable);
                return ResponseEntity.ok(transactions);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
