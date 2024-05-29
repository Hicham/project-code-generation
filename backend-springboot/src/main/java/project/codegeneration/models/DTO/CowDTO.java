package project.codegeneration.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CowDTO {
    private int id;
    private String name;
    private int age;
}
