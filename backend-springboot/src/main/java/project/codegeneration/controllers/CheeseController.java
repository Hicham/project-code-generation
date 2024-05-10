package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.AccountDTO;
import project.codegeneration.services.CheeseService;
import project.codegeneration.models.Cheese;
import project.codegeneration.models.DTO.CheeseDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CheeseController {

    private final CheeseService cheeseService;

    public CheeseController(final CheeseService cheeseService) {
        this.cheeseService = cheeseService;
    }

    @GetMapping("/cheeses")
    public List<CheeseDTO> getCheeses() {
        List<Cheese> cheeses = cheeseService.getAllCheeses();
        return cheeses.stream().map(cheese -> new CheeseDTO(cheese.getId(), cheese.getName(), cheese.getAge(), cheese.getCow().getName())).toList();
    }




}
