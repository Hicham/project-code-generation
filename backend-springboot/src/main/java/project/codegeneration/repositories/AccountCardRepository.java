package project.codegeneration.repositories;


import org.springframework.data.jpa.repository.Query;
import project.codegeneration.models.AccountCard;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface AccountCardRepository extends CrudRepository<AccountCard, Long> {

    Optional<AccountCard> findById(long id);


//    @Query("SELECT u.email FROM User u " +
//            "JOIN u.accounts a " +
//            "JOIN a.cards c " +
//            "WHERE c.id = ?1")

//    @Query("SELECT u.email FROM User u INNER JOIN u.accounts a WHERE a.IBAN = 'IBANFAKE1'")
@Query("SELECT u.email FROM User u INNER JOIN u.accounts where u.userId = 1")
    String findUserEmailByCardId(long id);
}
