package project.codegeneration.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.codegeneration.models.Account;
import project.codegeneration.models.DTO.TransactionLimitDTO;
import project.codegeneration.models.User;
import project.codegeneration.security.JwtProvider;
import project.codegeneration.services.AccountService;
import project.codegeneration.services.TransactionLimitService;
import project.codegeneration.services.UserService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionLimitService transactionLimitService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAccountsChecking() throws Exception {
        when(accountService.getCheckingAccountsByUserId(any(), anyInt())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/users/1/accounts/checking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAccountsByUser() throws Exception {
        when(accountService.getAccountsByUserId(any(), anyInt())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/users/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAccounts() throws Exception {
        when(accountService.getAllAccounts(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAccountByIBAN() throws Exception {
        Account account = new Account();
        account.setIBAN("IBAN123");
        when(accountService.getAccountByIBAN(anyString())).thenReturn(account);

        mockMvc.perform(get("/api/accounts/IBAN123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDisableAccount() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(accountService.getAccountByIBAN(anyString())).thenReturn(new Account());

        mockMvc.perform(post("/api/accounts/disable/IBAN123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Account disabled successfully."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testEnableAccount() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(accountService.getAccountByIBAN(anyString())).thenReturn(new Account());

        mockMvc.perform(post("/api/accounts/enable/IBAN123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Account enabled successfully."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSetTransactionLimits() throws Exception {
        TransactionLimitDTO transactionLimitDTO = new TransactionLimitDTO("IBAN", 500, 0);
        String json = "{\"absoluteLimit\":1000,\"dailyLimit\":500}";

        mockMvc.perform(post("/api/accounts/setLimits/IBAN123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction limits updated successfully"));
    }

//    private static CsrfToken csrfToken() {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        CsrfToken token = repository.generateToken(null);
//        return token;
//    }
}
