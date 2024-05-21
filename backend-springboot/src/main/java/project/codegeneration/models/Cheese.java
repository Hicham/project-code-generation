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
public class Cheese {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private int age;

    private double price;

    @ManyToOne
    private Cow cow;

}
