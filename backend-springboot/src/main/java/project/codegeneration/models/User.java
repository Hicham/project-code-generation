package project.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    private boolean isApproved;

    private String email;

    @JsonIgnore
    private String password;

    private String firstName;

    private String lastName;

    private String BSNNumber;

    private String phoneNumber;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Account> accounts = new ArrayList<>();

//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Transaction> transactions = new ArrayList<>();


    public User(List<Role> roles, boolean isApproved, String email, String password, String firstName, String lastName, String BSNNumber, String phoneNumber) {
        this.roles = roles;
        this.isApproved = isApproved;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.BSNNumber = BSNNumber;
        this.phoneNumber = phoneNumber;
    }


}
