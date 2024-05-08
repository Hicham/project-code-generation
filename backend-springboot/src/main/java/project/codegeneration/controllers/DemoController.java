package project.codegeneration.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public String demo() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Je hebt toegang " + auth.getName();
    }
}
