package git.darul.internet_banking.service.serviceImpl;

import git.darul.internet_banking.constants.Constants;
import git.darul.internet_banking.dto.request.TransactionRequest;
import git.darul.internet_banking.dto.response.GetBalanceResponse;
import git.darul.internet_banking.dto.response.TransactionResponse;
import git.darul.internet_banking.entity.Account;
import git.darul.internet_banking.entity.User;
import git.darul.internet_banking.repository.TransactionRepository;
import git.darul.internet_banking.service.AccountService;
import git.darul.internet_banking.service.BankService;
import git.darul.internet_banking.util.AuthenticationContextUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private BankService bankService;

    @Mock
    private AuthenticationContextUtil authenticationContextUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success_Internal() {
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        User mockUser = new User();
        Account mockAccount = Account.builder()
                .id("1")
                .accountNumber(1234567890)
                .name("John Doe")
                .balance(1000L)
                .build();
        mockUser.setAccount(mockAccount);

        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        try (MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

            when(accountService.getOne(anyString())).thenReturn(mockAccount);

            GetBalanceResponse receiverAccount = new GetBalanceResponse(
                    "1",
                    "Jane Doe",
                    987654321,
                    500L
            );
            when(accountService.getBalance(anyInt())).thenReturn(receiverAccount);

            doNothing().when(transactionRepository).insertTransaction(
                    anyString(), anyLong(), any(), anyString(), any(), anyString(), anyString(), anyString()
            );

            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setAmount(100L);
            transactionRequest.setBank("DARUL");
            transactionRequest.setToAccountNumber("987654321");

            TransactionResponse response = transactionService.createTransaction(transactionRequest);

            assertNotNull(response);
            assertEquals("Jane Doe", response.getReceiveAccountName());
            assertEquals("987654321", response.getReceiveAccountNumber());
            assertEquals(100L, response.getAmount());
        }
    }

    @Test
    void testCreateTransaction_Failure_LowBalance() {
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        User mockUser = new User();
        Account mockAccount = Account.builder()
                .id("1")
                .accountNumber(1234567890)
                .name("John Doe")
                .balance(50L)
                .build();
        mockUser.setAccount(mockAccount);

        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        try (MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

            when(accountService.getOne(anyString())).thenReturn(mockAccount);

            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setAmount(100L); // Melebihi saldo
            transactionRequest.setBank("DARUL");
            transactionRequest.setToAccountNumber("987654321");

            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> transactionService.createTransaction(transactionRequest)
            );

            assertNotNull(exception);
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
            assertEquals(Constants.BALANCE_LOW, exception.getReason());
        }
    }

    @Test
    void testGetTransactionById_NotFound() {
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        User mockUser = new User();
        Account senderAccount = new Account();
        senderAccount.setId("1");
        mockUser.setAccount(senderAccount);

        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

        try (MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

            when(transactionRepository.getById("invalid-id", senderAccount.getId())).thenReturn(Optional.empty());

            ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                    () -> transactionService.getTransactionById("invalid-id"));
            assertEquals("404 NOT_FOUND \"Transaction not found\"", exception.getMessage());
        }
    }

}
