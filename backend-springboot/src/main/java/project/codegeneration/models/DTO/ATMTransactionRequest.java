package project.codegeneration.models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ATMTransactionRequest {

    private double amount;

    private String description;
}
