package project.codegeneration.repositories;

<<<<<<< HEAD

=======
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.codegeneration.models.Account;
import project.codegeneration.models.AccountType;
import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
public interface AccountRepository extends JpaRepository<Account, Integer> {
=======
import java.util.List;
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByIBAN(String IBAN);

    List<Account> findByUserId(int id);

    List<Account> findByUserIdAndAccountType(Integer userId, AccountType accountType);

    Account findByIBAN(String iban);
}
