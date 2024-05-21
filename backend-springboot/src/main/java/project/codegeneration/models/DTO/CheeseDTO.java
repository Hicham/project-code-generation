package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheeseDTO {
    private int id;
    private String name;
    private int age;
    private String cowName;

}
