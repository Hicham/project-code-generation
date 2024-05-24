package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequestDTO {
    private String sourceIBAN;
    private String destinationIBAN;
    private Double amount;

}
