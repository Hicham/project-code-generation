package project.codegeneration.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String IBAN;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TransactionLimit transactionLimit;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private AccountType accountType;

    private double balance;

    private boolean isActive;

    private double absoluteLimit;




}
