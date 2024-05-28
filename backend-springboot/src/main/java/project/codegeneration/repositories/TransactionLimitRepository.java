package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.TransactionLimit;

public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, String> {
    TransactionLimit findByIBAN(String iban);
}
