package project.codegeneration.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public AccountController(final AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts(@RequestParam(required = false) Integer userId, Boolean isChecking) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));

        Optional<User> currentUser = userService.findByEmail(currentUsername);


        List<Account> accounts = null;

        if (userId != null) {

            if(isAdmin || currentUser.get().getId() == userId)
            {
                if (isChecking)
                {
                    accounts = accountService.getCheckingAccountsByUserId(userId);
                }
                else
                {
                    accounts = accountService.getAccountsByUserId(userId);
                }
            }

        } else {

            if (isAdmin)
            {
                accounts = accountService.getAllAccounts();
            }
        }


        List<AccountDTO> accountDTOs = accounts.stream()
                .map(account -> new AccountDTO(account.getIBAN(), account.getUser().getId(), account.getAccountType().toString(), account.getBalance()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDTOs);
    }

    @GetMapping("/accounts/{IBAN}")
    public ResponseEntity<AccountDTO> getAccountByIBAN(@PathVariable String IBAN, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(null);
        }

        String jwt = bearerToken.substring(7);
        System.out.println(jwt);

        Account account = accountService.getAccountByIBAN(IBAN);

        if (account == null) {
            return ResponseEntity.notFound().build();
        } else {
            AccountDTO accountDTO = new AccountDTO(account.getIBAN(), account.getUser().getId(), account.getAccountType().toString(), account.getBalance());
            return ResponseEntity.ok(accountDTO);
        }
    }

//    @PostMapping("/account/{iban}/withdraw")
//    public ResponseEntity<String> withdraw(@PathVariable String iban, @RequestParam double amount, HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUsername = authentication.getName();
//        Optional<User> currentUser = userService.findByEmail(currentUsername);
//
//        Account account = accountService.getAccountByIBAN(iban);
//
//        if (account == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (currentUser.isPresent() && (currentUser.get().getId().equals(account.getUser().getId()) || isAdmin(authentication))) {
//            boolean success = accountService.withdraw(iban, amount);
//            if (success) {
//                return ResponseEntity.ok("Withdrawal successful");
//            } else {
//                return ResponseEntity.badRequest().body("Insufficient funds or invalid amount");
//            }
//        } else {
//            return ResponseEntity.status(403).body("Access denied");
//        }
//    }

    @PostMapping("/accounts/{IBAN}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String IBAN, @RequestBody double amount
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByEmail(currentUsername);

        Account account = accountService.getAccountByIBAN(IBAN);

        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        if (currentUser.isPresent() && currentUser.get().getId() == account.getUser().getId() || isAdmin(authentication)) {
            boolean success = accountService.deposit(account, amount);
            if (success) {
                return ResponseEntity.ok("Deposit successful");
            } else {
                return ResponseEntity.badRequest().body("Invalid amount");
            }
        } else {
            return ResponseEntity.status(403).body("Access denied");
        }
    }

    private boolean isAdmin(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString()));
    }
}
