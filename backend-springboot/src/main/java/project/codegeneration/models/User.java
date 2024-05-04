package project.codegeneration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private int UserId;

    @ManyToOne
    private Role RoleId;

    private boolean IsApproved;

    private String Email;

    private String Password;

    private String FirstName;

    private String LastName;

    private String BSNNumber;

    private int PhoneNumber;

    private int PinCode;

}
