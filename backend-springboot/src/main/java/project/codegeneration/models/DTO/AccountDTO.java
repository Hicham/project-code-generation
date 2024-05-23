package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.codegeneration.models.User;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String IBAN;
//    private int UserId;
    private User user;
    private String AccountType;
    private double Balance;
//    private int pinCode;
    private boolean isActive;
    private double absoluteLimit;
}
