package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.codegeneration.models.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}