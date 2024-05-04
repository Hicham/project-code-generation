package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
