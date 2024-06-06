package project.codegeneration.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findAll();

    Account findByIBAN(String IBAN);

    Page<Account> findByUserId(Pageable pageable, int id);

    Page<Account> findByUserIdAndAccountType(Pageable pageable, Integer userId, AccountType accountType);

    Optional<Account> findAccountsByUserEmail(String email);

}
