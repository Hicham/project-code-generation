package project.codegeneration.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByEmail(String email);

    //Not approved accounts
    @Query("SELECT u FROM User u WHERE u.isApproved = false")
    List<User> findNotApproved();

    //new query
//    @Query("SELECT u FROM User u WHERE u.isApproved = false AND u.roles = :role")
//    List<User> findNotApproved(@Param("role") Role role);
}
