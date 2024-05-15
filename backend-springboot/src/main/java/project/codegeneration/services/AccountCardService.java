package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.AccountCard;
import project.codegeneration.repositories.AccountCardRepository;
import project.codegeneration.security.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class AccountCardService {

    private final AccountCardRepository accountCardRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;


    public AccountCardService(AccountCardRepository accountCardRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider) {
        this.accountCardRepository = accountCardRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AccountCard create(AccountCard card) {
        card.setPincode(bCryptPasswordEncoder.encode(card.getPincode()));
        return accountCardRepository.save(card);
    }

    public Optional<AccountCard> findById(int id){
        return accountCardRepository.findById(id);
    }

    public String login(int id, String pincode){
        AccountCard card = accountCardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (!bCryptPasswordEncoder.matches(pincode, card.getPincode())) {
            throw new IllegalArgumentException("Invalid pincode");
        }

        return jwtProvider.createTokenFromCard(id, card.getPasHolderName());
    }
}
