package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByIBAN(String IBAN);

    List<Account> findByUserId(int id);

    List<Account> findByUserIdAndAccountType(Integer userId, AccountType accountType);
}
