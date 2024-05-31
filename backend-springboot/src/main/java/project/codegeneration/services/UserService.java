package project.codegeneration.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.repositories.UserRepository;
import project.codegeneration.security.JwtProvider;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
    }


    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User create(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return jwtProvider.createToken(user);
    }

    public List<User> getNotApprovedUsers() {
        return userRepository.findByIsApprovedAndRolesContains(false, Role.ROLE_USER);
    }

    public void approveUser(int userId) {
        User user = userRepository.findById((long) userId).orElseThrow();
        user.setApproved(true);
        userRepository.save(user);
        userRepository.flush();
    }

    public User getUserById(int userId) {
        return userRepository.findById((long) userId).orElseThrow();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }


    public Page<User> getUsers(Pageable pageable, String email) {

        if (email.isBlank())
        {
            return userRepository.findAll(pageable);
        }
        else
        {
            return userRepository.findByEmailContaining(pageable, email);
        }
    }

}
