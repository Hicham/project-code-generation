package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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




}
