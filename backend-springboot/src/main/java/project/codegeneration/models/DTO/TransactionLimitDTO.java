package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionLimitDTO {
    private int id;
    private double limit;
    private String type;
    private double userId;
}
