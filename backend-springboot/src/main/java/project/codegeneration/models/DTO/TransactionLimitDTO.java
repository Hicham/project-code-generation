package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionLimitDTO {
    private String IBAN;
    private double dailyLimit;
    private double weeklyLimit;
    private double monthlyLimit;
}

