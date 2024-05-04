package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private int UserId;
    private String RoleName;
    private boolean IsApproved;
    private String Email;
    private String Password;
    private String FirstName;
    private String LastName;
    private String BSNNumber;
    private int PhoneNumber;
    private int PinCode;
}
