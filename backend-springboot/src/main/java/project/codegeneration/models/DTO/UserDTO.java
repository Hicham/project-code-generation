package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private int userId;
    private String roleName;
    private boolean isApproved;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String BSNNumber;
    private int phoneNumber;
    private int pinCode;
}