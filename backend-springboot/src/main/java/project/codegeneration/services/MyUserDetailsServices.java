package project.codegeneration.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.codegeneration.models.CustomUserDetails;
import project.codegeneration.models.User;
import project.codegeneration.repositories.UserRepository;

@Service
public class MyUserDetailsServices implements UserDetailsService {


    private UserRepository userRepository;

    public MyUserDetailsServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
//
//        UserDetails userDetails = org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                .authorities(user.getRoles())
//                .build();
//
//        return userDetails;
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails((long) user.getId(), user.getEmail(), user.getPassword(), user.getRoles());
    }
}
