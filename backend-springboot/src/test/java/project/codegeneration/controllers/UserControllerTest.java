package project.codegeneration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.codegeneration.models.DTO.ApproveUserDTO;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.models.DTO.UserRegisterDTO;
import project.codegeneration.models.User;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetNotApprovedUsers() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.getNotApprovedUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/users/unapproved"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(userService).getNotApprovedUsers();
    }

    @Test
    void testRegisterUser() throws Exception {
        UserRegisterDTO userDTO = new UserRegisterDTO(1, "USER", false, "test@gmail.com", "Test123", "test", "test", "36251", "063524152");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        verify(userService).create(any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserByEmail() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService).findByEmail("test@example.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testApproveUser() throws Exception {
        int userId = 1;
        ApproveUserDTO approveUserDTO = new ApproveUserDTO(userId, new TransactionLimitDTO("IBANFAKE1", 1000, 0), 0);

        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users/{id}/approve", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(approveUserDTO)))
                .andExpect(status().isOk());

        verify(userService).approveUser(userId);
        verify(accountService).createAccountForApprovedUser(eq(user), eq(approveUserDTO));
    }
}
