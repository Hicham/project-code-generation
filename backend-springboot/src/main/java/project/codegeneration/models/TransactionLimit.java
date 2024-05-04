package project.codegeneration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TransactionLimit {
    @Id
    private int UserId;

    private double Limit;
    private String LimitType;
    private double Amount;
}
