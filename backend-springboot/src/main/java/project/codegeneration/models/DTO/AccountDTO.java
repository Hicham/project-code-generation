package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.codegeneration.models.TransactionLimit;
import project.codegeneration.models.User;
import project.codegeneration.models.AccountType;
import project.codegeneration.services.TransactionLimitService;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String IBAN;
    private User user;
    private AccountType AccountType;
    private double Balance;
    private boolean isActive;
    private double absoluteLimit;
    private TransactionLimit transactionLimit;
}
