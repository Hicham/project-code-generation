package project.codegeneration.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String description;
    private TransactionType type;

    private Long timestamp;
    //    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Transaction(String sourceIBAN, String destinationIBAN, Double amount, String description, TransactionType type, User user) {
        this.sourceIBAN = sourceIBAN;
        this.destinationIBAN = destinationIBAN;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.user = user;
        this.timestamp = System.currentTimeMillis();
    }
}
