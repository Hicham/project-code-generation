package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.codegeneration.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
