package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
<<<<<<< HEAD
import project.codegeneration.models.User;
import project.codegeneration.models.AccountType;
=======
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)

@Data
@AllArgsConstructor
public class AccountDTO {
    private String IBAN;
    private User user;
    private AccountType AccountType;
    private double Balance;
//    private int pinCode;
    private boolean isActive;
    private double absoluteLimit;
}
