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
import org.thymeleaf.util.CharArrayWrapperSequence;
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.models.DTO.ATMTransactionRequest;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.models.Role;
import project.codegeneration.models.TransactionType;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionLimitService;
import project.codegeneration.services.TransactionService;
import project.codegeneration.services.UserService;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController extends Controller {

    private final AccountService accountService;
    private final UserService userService;
    private final TransactionLimitService transactionLimitService;

    public AccountController(AccountService accountService, UserService userService, TransactionLimitService transactionLimitService) {
        super(userService);
        this.userService = userService;
        this.accountService = accountService;
        this.transactionLimitService = transactionLimitService;
    }


    @GetMapping("/users/{userId}/accounts/checking")
    public ResponseEntity<Page<AccountDTO>> getAccountsChecking(@PathVariable Integer userId){
        try {

            Optional<User> user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

            if (user.get().getId() != userId || user.get().getRoles().contains(Role.ROLE_ADMIN))
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            Page<Account> accounts = null;
            Pageable pageable = PageRequest.of(0, 10);
            accounts = accountService.getCheckingAccountsByUserId(pageable, userId);

            Page<AccountDTO> accountDTOPage = accounts.map(account -> new AccountDTO(
                    account.getIBAN(),
                    account.getUser(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.isActive(),
                    account.getAbsoluteLimit(),
                    account.getTransactionLimit()
            ));

            return ResponseEntity.ok(accountDTOPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<Page<AccountDTO>> getAccountsByUser(@RequestParam(required = false, defaultValue = "0") Integer pageNumber, @PathVariable Integer userId){
        try {

            Optional<User> user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

            if (user.get().getId() != userId || user.get().getRoles().contains(Role.ROLE_ADMIN))
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Page<Account> accounts = null;
            Pageable pageable = PageRequest.of(pageNumber, 10);
            accounts = accountService.getAccountsByUserId(pageable, userId);

            Page<AccountDTO> accountDTOPage = accounts.map(account -> new AccountDTO(
                    account.getIBAN(),
                    account.getUser(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.isActive(),
                    account.getAbsoluteLimit(),
                    account.getTransactionLimit()
            ));

            return ResponseEntity.ok(accountDTOPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountDTO>> getAccounts(@RequestParam(required = false, defaultValue = "0") Integer pageNumber) {

        try {

            Page<Account> accounts = null;
            Pageable pageable = PageRequest.of(pageNumber, 10);
            accounts = accountService.getAllAccounts(pageable);

            Page<AccountDTO> accountDTOPage = accounts.map(account -> new AccountDTO(
                    account.getIBAN(),
                    account.getUser(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.isActive(),
                    account.getAbsoluteLimit(),
                    account.getTransactionLimit()
            ));

            return ResponseEntity.ok(accountDTOPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/accounts/{IBAN}")
    public ResponseEntity<AccountDTO> getAccountByIBAN(@PathVariable String IBAN) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Account account = accountService.getAccountByIBAN(IBAN);

        if (account == null) {
            return ResponseEntity.notFound().build();
        } else {
            AccountDTO accountDTO = new AccountDTO(account.getIBAN(), account.getUser(), account.getAccountType(), account.getBalance(), account.isActive(), account.getAbsoluteLimit(), account.getTransactionLimit());
            return ResponseEntity.ok(accountDTO);
        }
    }



    @PostMapping("/accounts/disable/{IBAN}")
    public ResponseEntity<String> disableAccount(@PathVariable String IBAN) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByEmail(currentUsername);

        if (currentUser.isPresent()) {
            Account account = accountService.getAccountByIBAN(IBAN);
            if (account != null) {
                account.setActive(false);
                accountService.updateAccount(account);
                return ResponseEntity.ok("Account disabled successfully.");
            }
            else
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
            }
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

    }

    @PostMapping("/accounts/enable/{IBAN}")
    public ResponseEntity<String> enableAccount(@PathVariable String IBAN) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByEmail(currentUsername);

        if (currentUser.isPresent()) {
            Account account = accountService.getAccountByIBAN(IBAN);
            if (account != null) {
                account.setActive(true);
                accountService.updateAccount(account);
                return ResponseEntity.ok("Account enabled successfully.");
            }
            else
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
            }
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        }

    }

    @PostMapping("/accounts/setLimits/{iban}")
    public ResponseEntity<String> setTransactionLimits(
            @PathVariable String iban,
            @RequestBody TransactionLimitDTO transactionLimitDTO) {
        accountService.setAbsoluteLimit(iban, transactionLimitDTO.getAbsoluteLimit());
        transactionLimitService.setTransactionLimit(
                iban,
                transactionLimitDTO.getDailyLimit(),
                transactionLimitDTO.getWeeklyLimit(),
                transactionLimitDTO.getMonthlyLimit()
        );
        return ResponseEntity.ok("Transaction limits updated successfully");
    }


//    private boolean isAdmin(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return userDetails.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));
//    }


//   @GetMapping("/accounts/users/{email}")
//   public AccountDTO getAccountsByUserEmail(@PathVariable String email) {
//       Optional<Account> accounts = accountService.getAccountsByUserEmail(email);
//       if (accounts.isPresent()) {
//           Account account = accounts.get();
//           return new AccountDTO(account.getIBAN(), account.getUser(), account.getAccountType(), account.getBalance(), account.isActive(), account.getAbsoluteLimit());
//       }
//       else {
//           return null;
//       }
//
//   }

    @GetMapping("/accounts/all")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountDTO> accountDTOs = accounts.stream().map(account -> new AccountDTO(
                account.getIBAN(),
                account.getUser(),
                account.getAccountType(),
                account.getBalance(),
                account.isActive(),
                account.getAbsoluteLimit(),
                account.getTransactionLimit()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOs);
    }


}
