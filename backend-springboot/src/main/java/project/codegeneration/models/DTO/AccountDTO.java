package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.codegeneration.models.User;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String IBAN;
    private User user;
    private String accountType;
    private double balance;
}
