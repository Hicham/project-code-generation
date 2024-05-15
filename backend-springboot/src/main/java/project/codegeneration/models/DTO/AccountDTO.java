package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String IBAN;
//    private int UserId;
    private String AccountType;
    private double Balance;
}
