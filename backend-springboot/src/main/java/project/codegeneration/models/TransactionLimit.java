package project.codegeneration.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TransactionLimit {
    @Id
    private String IBAN;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    private double dailyLimit;
}
