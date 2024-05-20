package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
