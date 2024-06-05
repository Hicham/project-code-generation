package project.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    private double dailyLimit;

    private double weeklyLimit;

    private double monthlyLimit;
}
