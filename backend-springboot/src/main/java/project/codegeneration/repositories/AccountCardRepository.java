package project.codegeneration.repositories;


import project.codegeneration.models.AccountCard;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface AccountCardRepository extends CrudRepository<AccountCard, Integer> {

    Optional<AccountCard> findById(int id);

}
