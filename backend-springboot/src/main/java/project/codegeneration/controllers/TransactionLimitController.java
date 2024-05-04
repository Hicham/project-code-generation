package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.models.TransactionLimit;
import project.codegeneration.services.TransactionLimitService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionLimitController {
    private final TransactionLimitService transactionLimitService;

    public TransactionLimitController(final TransactionLimitService transactionLimitService) {
        this.transactionLimitService = transactionLimitService;
    }

}
