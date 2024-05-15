package project.codegeneration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AccountCard
{
    @Id
    @GeneratedValue
    private long id;

    private String pincode;

    private String pasHolderName;

    public AccountCard(String pincode, String pasHolderName) {
        this.pincode = pincode;
        this.pasHolderName = pasHolderName;
    }
}
