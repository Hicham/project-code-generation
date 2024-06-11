package project.codegeneration.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.codegeneration.exceptions.ResourceNotFoundException;
import project.codegeneration.models.Role;
import project.codegeneration.models.User;
import project.codegeneration.repositories.UserRepository;
import project.codegeneration.security.JwtProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testFindByEmail_UserNotExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail("test@example.com"));
    }

    @Test
    void testCreate() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("Test123");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }



    @Test
    void testLogin_ValidCredentials() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(jwtProvider.createToken(any(User.class))).thenReturn("token");

        String token = userService.login("test@example.com", "password");
        assertNotNull(token);
        assertEquals("token", token);
    }

    @Test
    void testLogin_InvalidPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("invalidPassword", "hashedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.login("test@example.com", "invalidPassword"));
    }

    @Test
    void testGetNotApprovedUsers() {
        when(userRepository.findByIsApprovedAndRolesContains(false, Role.ROLE_USER)).thenReturn(Collections.singletonList(new User()));

        List<User> notApprovedUsers = userService.getNotApprovedUsers();
        assertNotNull(notApprovedUsers);
        assertEquals(1, notApprovedUsers.size());
    }

    @Test
    void testApproveUser() {
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.approveUser(1L);
        assertTrue(user.isApproved());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetUserByEmail_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@example.com");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetUsers_NoEmailProvided() {
        Page<User> page = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<User> result = userService.getUsers(PageRequest.of(0, 10), "");
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetUsers_EmailProvided() {
        Page<User> page = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findByEmailContaining(any(Pageable.class), anyString())).thenReturn(page);

        Page<User> result = userService.getUsers(PageRequest.of(0, 10), "test@example.com");
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
