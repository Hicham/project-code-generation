package project.codegeneration.repositories;

import project.codegeneration.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
