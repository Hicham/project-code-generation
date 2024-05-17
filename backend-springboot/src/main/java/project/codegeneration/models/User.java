package project.codegeneration.models;

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
    @GeneratedValue
    private int id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    private boolean isApproved;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String BSNnumber;

    private String phoneNumber;

    private int pinCode;



}
