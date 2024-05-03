package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.Cow;
import project.codegeneration.repositories.CowRepository;

import java.util.List;

@Service
public class CowService {

    private final CowRepository cowRepository;

    public CowService(CowRepository cowRepository) {
        this.cowRepository = cowRepository;
    }

    public Cow getCowById(int id) {
        return cowRepository.findById(id).orElseThrow();
    }

    public List<Cow> getAllCows() {
        return cowRepository.findAll();
    }

    public Cow updateCow(Cow cow) {
        return cowRepository.save(cow);
    }

    public Cow createCow(Cow cow) {
        return cowRepository.save(cow);
    }

    public void deleteCow(int id) {
        cowRepository.deleteById(id);
    }


}
