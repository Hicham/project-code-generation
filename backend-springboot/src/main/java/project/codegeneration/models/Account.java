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
public class Account {

    @Id
    private String IBAN;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TransactionLimit transactionLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private AccountType accountType;

    private double balance;

    private boolean isActive;

    private double absoluteLimit;

}
