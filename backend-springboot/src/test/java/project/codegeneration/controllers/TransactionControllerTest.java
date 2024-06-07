//package project.codegeneration.controllers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import project.codegeneration.exceptions.ResourceNotFoundException;
//import project.codegeneration.models.DTO.ATMTransactionRequest;
//import project.codegeneration.models.User;
//import project.codegeneration.security.JwtProvider;
//import project.codegeneration.services.AccountService;
//import project.codegeneration.services.TransactionService;
//import project.codegeneration.services.UserService;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(TransactionController.class)
//public class TransactionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JwtProvider jwtProvider;
//
//    @MockBean
//    private TransactionService transactionService;
//
//    @InjectMocks
//    private TransactionController transactionController;
//
//    @MockBean
//    private AccountService accountService;
//
//    @MockBean
//    private UserService userService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testGetTransactions() throws Exception {
//        mockMvc.perform(get("/api/transactions"))
//                .andExpect(status().isOk());
//
//        verify(transactionService, times(1)).getTransactions(any());
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void testGetAccountTransactions() throws Exception {
//        mockMvc.perform(get("/api/accounts/123456/transactions"))
//                .andExpect(status().isOk());
//
//        verify(transactionService, times(1)).getAccountTransactions(
//                anyString(),
//                nullable(String.class),
//                nullable(String.class),
//                nullable(Double.class),
//                nullable(String.class),
//                nullable(String.class),
//                nullable(String.class),
//                any());
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "USER"})
//    public void testCreateTransaction() throws Exception {
//        when(userService.getUserByEmail("hicham@gmail.com")).thenReturn(new User());
//
//        mockMvc.perform(post("/api/transactions")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"sourceIBAN\":\"sourceIBAN\",\"destinationIBAN\":\"destinationIBAN\",\"amount\":100.0,\"description\":\"description\"}"))
//                .andExpect(status().isCreated());
//
//        verify(transactionService, times(1)).transferTransaction(anyString(), anyString(), anyDouble(), anyString(), any(), any());
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "USER"})
//    public void testDeposit() throws Exception {
//        mockMvc.perform(post("/api/accounts/IBAN/deposit")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"amount\":100.0,\"description\":\"description\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Money deposited successfully."));
//
//        verify(transactionService, times(1)).ATMTransaction(any(), anyString(), anyDouble(), anyString(), any(), any());
//    }
//
//
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "USER"})
//    public void testDepositWithNonexistentIBAN() throws Exception {
//        doThrow(new ResourceNotFoundException("Cant find source account"))
//                .when(transactionService).ATMTransaction(any(), anyString(), anyDouble(), anyString(), any(), any());
//
//        mockMvc.perform(post("/api/accounts/INVALID_IBAN/deposit")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"amount\":100.0,\"description\":\"description\"}"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Cant find source account"));
//
//        verify(transactionService, times(1)).ATMTransaction(any(), eq("INVALID_IBAN"), anyDouble(), anyString(), any(), any());
//    }
//
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "USER"})
//    public void testWithdraw() throws Exception {
//        ATMTransactionRequest atmTransactionRequest = new ATMTransactionRequest();
//        atmTransactionRequest.setAmount(100.0);
//        atmTransactionRequest.setDescription("description");
//
//        mockMvc.perform(post("/api/accounts/123456/withdraw")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"amount\":100.0,\"description\":\"description\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Money deposited successfully."));
//
//        verify(transactionService, times(1)).ATMTransaction(anyString(), any(), anyDouble(), anyString(), any(), any());
//    }
//}
