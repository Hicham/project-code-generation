package project.codegeneration.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.codegeneration.models.Transaction;

import java.util.Optional;


public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page findBySourceIBANOrDestinationIBANOrderByTimestampDesc(String sourceIBAN, String destinationIBAN, Pageable pageable);

//    List<Transaction> findBySourceIBANAndTimestampBetween(String sourceIBAN, Long startDate, Long endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.sourceIBAN = :sourceIBAN AND t.timestamp BETWEEN :startDate AND :endDate AND t.type = 2")
    Optional<Double> findSumBySourceIBANAndTimestampBetween(@Param("sourceIBAN") String sourceIBAN, @Param("startDate") Long startDate, @Param("endDate") Long endDate);

}
