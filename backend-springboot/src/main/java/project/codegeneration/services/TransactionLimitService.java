package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.TransactionLimit;
import project.codegeneration.repositories.TransactionLimitRepository;

@Service
public class TransactionLimitService{
    private final TransactionLimitRepository transactionLimitRepository;

    public TransactionLimitService(TransactionLimitRepository transactionLimitRepository){
        this.transactionLimitRepository = transactionLimitRepository;
    }
    public void setTransactionLimit(String IBAN, double dailyLimit){
        TransactionLimit transactionLimit = transactionLimitRepository.findByIBAN(IBAN);
        transactionLimit.setDailyLimit(dailyLimit);
        transactionLimitRepository.save(transactionLimit);
    }
}
