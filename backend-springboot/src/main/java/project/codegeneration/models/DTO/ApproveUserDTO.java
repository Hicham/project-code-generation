package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveUserDTO {
    private int userId;
    private TransactionLimitDTO transactionLimit;
    private double absoluteLimit;
}
