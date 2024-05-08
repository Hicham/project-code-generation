package project.codegeneration.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import project.codegeneration.services.CowService;
import project.codegeneration.models.Cow;
import project.codegeneration.models.DTO.CowDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CowController {

    private final CowService cowService;

    public CowController(final CowService cowService) {
        this.cowService = cowService;
    }


}
