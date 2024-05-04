package project.codegeneration.services;

import org.springframework.stereotype.Service;
import project.codegeneration.models.User;
import project.codegeneration.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

        private final UserRepository userRepository;

        public UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public User getUserById(int id) {
            return userRepository.findById(id).orElseThrow();
        }

        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        public User updateUser(User user) {
            return userRepository.save(user);
        }

        public User createUser(User user) {
            return userRepository.save(user);
        }

        public void deleteUser(int id) {
            userRepository.deleteById(id);
        }
}
