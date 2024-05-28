package project.codegeneration.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);

    //Get not approved accounts
    @Query("SELECT u FROM User u WHERE u.isApproved = false AND :role MEMBER OF u.roles")
    List<User> findNotApproved(@Param("role") Role role);

    //get unapproved users
    List<User> findByIsApprovedAndRolesContains(boolean isApproved, Role role);

    Page<User> findByEmailContaining(Pageable pageable, String email);

}
