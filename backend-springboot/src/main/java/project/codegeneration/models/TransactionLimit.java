package project.codegeneration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TransactionLimit {
    //foreign key
    @Id
    private String IBAN;

    private double dailyLimit;

    private double weeklyLimit;

    private double monthlyLimit;
}
