package git.darul.internet_banking.service.serviceImpl;

import git.darul.internet_banking.constants.Constants;
import git.darul.internet_banking.dto.response.GetBalanceResponse;
import git.darul.internet_banking.entity.Account;
import git.darul.internet_banking.repository.AccountRepository;
import git.darul.internet_banking.util.AuthenticationContextUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuthenticationContextUtil authenticationContextUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetOne_Success() {
        // Mocking
        String accountId = "test-account-id";
        Account mockAccount = Account.builder()
                .id(accountId)
                .name("John Doe")
                .balance(1000L)
                .accountNumber(12345)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Call method
        Account account = accountService.getOne(accountId);

        // Assertions
        assertNotNull(account);
        assertEquals("John Doe", account.getName());
        assertEquals(1000L, account.getBalance());
        assertEquals(12345, account.getAccountNumber());

        // Verify interaction
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testGetOne_NotFound() {
        String accountId = "non-existent-id";

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.getOne(accountId));
        assertEquals("Account with id " + accountId + " not found", exception.getMessage());

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testGetBalance_Success() {
        // Mocking
        Integer accountNumber = 12345;
        Account mockAccount = Account.builder()
                .id("test-account-id")
                .name("John Doe")
                .balance(1000L)
                .accountNumber(accountNumber)
                .build();

        when(accountRepository.findBalanceByAccountNumber(accountNumber)).thenReturn(Optional.of(mockAccount));

        // Call method
        GetBalanceResponse response = accountService.getBalance(accountNumber);

        // Assertions
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals(1000L, response.getBalance());
        assertEquals(accountNumber, response.getAccountNumber());

        // Verify interactions
        verify(accountRepository, times(1)).findBalanceByAccountNumber(accountNumber);
    }

    @Test
    void testGetBalance_NotFound() {
        // Mocking
        Integer accountNumber = 12345;

        when(accountRepository.findBalanceByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        // Assert exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> accountService.getBalance(accountNumber));

        assertEquals(Constants.FAILED_FETCH_BALANCE, exception.getReason());
        assertEquals(404, exception.getStatusCode().value());

        // Verify interaction
        verify(accountRepository, times(1)).findBalanceByAccountNumber(accountNumber);
    }
}
