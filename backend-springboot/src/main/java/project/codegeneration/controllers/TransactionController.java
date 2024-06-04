package project.codegeneration.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.*;
import project.codegeneration.models.DTO.ATMTransactionRequest;
import project.codegeneration.models.DTO.TransactionRequestDTO;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionService;
import project.codegeneration.services.UserService;
import project.codegeneration.models.TransactionType;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController extends Controller{


    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;


    public TransactionController(TransactionService transactionService, AccountService accountService, UserService userService) {
        super(userService);
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(@RequestParam(required = false, defaultValue = "0") Integer pageNumber) {

        try {
            Pageable pageable = PageRequest.of(pageNumber, 10);
            return ResponseEntity.ok(transactionService.getTransactions(pageable));

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

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequestDTO transactionRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Assuming the user is always present in the authentication details
            User user = userService.getUserByEmail(userDetails.getUsername());


            // Validate the request
            if (transactionRequest.getAmount() <= 0) {
                return ResponseEntity.badRequest().body("Invalid amount");
            }

            if (transactionRequest.getSourceIBAN() == null || transactionRequest.getDestinationIBAN() == null) {
                return ResponseEntity.badRequest().body("Source and destination IBANs are required");
            }

            TransactionType type = TransactionType.TRANSFER;

            // Perform the transaction
            transactionService.transferTransaction(
                    transactionRequest.getSourceIBAN(),
                    transactionRequest.getDestinationIBAN(),
                    transactionRequest.getAmount(),
                    transactionRequest.getDescription(),
                    type,
                    user
            );

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/accounts/{IBAN}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String IBAN, @RequestBody ATMTransactionRequest ATMTransactionRequest
    ) {
        Optional<User> user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        if (user.isPresent()) {
            transactionService.ATMTransaction(null, IBAN, ATMTransactionRequest.getAmount(), ATMTransactionRequest.getDescription(), TransactionType.DEPOSIT, user.get());
            return ResponseEntity.ok("Money deposited successfully.");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

    }

    @PostMapping("/accounts/{IBAN}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String IBAN, @RequestBody ATMTransactionRequest ATMTransactionRequest) {

        Optional<User> user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        if (user.isPresent()) {
            transactionService.ATMTransaction(IBAN, null, ATMTransactionRequest.getAmount(), ATMTransactionRequest.getDescription(), TransactionType.DEPOSIT, user.get());
            return ResponseEntity.ok("Money deposited successfully.");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

    }



}
