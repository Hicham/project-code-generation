package project.codegeneration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cow {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private int age;

    @OneToMany
    private Set<Cheese> cheeses = new HashSet<>();
}

