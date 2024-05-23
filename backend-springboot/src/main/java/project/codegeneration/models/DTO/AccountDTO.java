package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.codegeneration.models.User;
import project.codegeneration.models.AccountType;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String IBAN;
    private User user;
    private String AccountType;
    private AccountType AccountType;
    private double Balance;
    private boolean isActive;
    private double absoluteLimit;
}
