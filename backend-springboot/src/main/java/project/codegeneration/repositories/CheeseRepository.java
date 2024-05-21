package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Cheese;

public interface CheeseRepository extends JpaRepository<Cheese, Integer> {


}
