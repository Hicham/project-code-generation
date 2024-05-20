package project.codegeneration.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceIBAN;
    private String destinationIBAN;
    private Double amount;
    private TransactionType type;

    private Long timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    public Transaction(String sourceIBAN, String destinationIBAN, Double amount, TransactionType type, Long timestamp, User user) {
        this.sourceIBAN = sourceIBAN;
        this.destinationIBAN = destinationIBAN;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.user = user;
    }
}
