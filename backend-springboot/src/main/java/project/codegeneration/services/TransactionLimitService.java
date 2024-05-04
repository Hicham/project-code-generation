package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.repositories.TransactionLimitRepository;

@Service
public class TransactionLimitService {
    private final TransactionLimitRepository transactionLimitRepository;

    public TransactionLimitService(final TransactionLimitRepository transactionLimitRepository) {
        this.transactionLimitRepository = transactionLimitRepository;
    }

    public void updateTransactionLimit(final double transactionLimit) {
        transactionLimitRepository.updateTransactionLimit(transactionLimit);
    }
}
