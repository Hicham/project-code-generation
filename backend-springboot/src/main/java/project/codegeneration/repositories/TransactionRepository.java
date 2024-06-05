package project.codegeneration.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Transaction;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page findBySourceIBANOrDestinationIBANOrderByTimestampDesc(String sourceIBAN, String destinationIBAN, Pageable pageable);

    List<Transaction> findBySourceIBANAndTimestampBetween(String sourceIBAN, LocalDateTime startDate, LocalDateTime endDate);
}
