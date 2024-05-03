package project.codegeneration.services;


import org.springframework.stereotype.Service;
import project.codegeneration.repositories.CheeseRepository;
import project.codegeneration.models.Cheese;

import java.util.List;

@Service
public class CheeseService {

        private final CheeseRepository cheeseRepository;

        public CheeseService(CheeseRepository cheeseRepository) {
            this.cheeseRepository = cheeseRepository;
        }

        public Cheese getCheeseById(int id) {
            return cheeseRepository.findById(id).orElseThrow();
        }

        public List<Cheese> getAllCheeses() {
            return cheeseRepository.findAll();
        }

        public Cheese updateCheese(Cheese cheese) {
            return cheeseRepository.save(cheese);
        }

        public Cheese createCheese(Cheese cheese) {
            return cheeseRepository.save(cheese);
        }

        public void deleteCheese(int id) {
            cheeseRepository.deleteById(id);
        }

}
