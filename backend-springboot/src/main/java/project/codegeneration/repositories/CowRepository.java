package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.Cow;

public interface CowRepository extends JpaRepository<Cow, Integer> {


}
