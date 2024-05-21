package project.codegeneration.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;

import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findAll();

    Optional<Account> findAccountsByUserEmail(String email);

    List<Account> findByUserId(int id);

    List<Account> findByUserIdAndAccountType(Integer userId, AccountType accountType);

    Account findByIBAN(String iban);
}
