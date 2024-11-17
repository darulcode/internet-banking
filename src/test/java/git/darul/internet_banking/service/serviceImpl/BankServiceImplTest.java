package git.darul.internet_banking.service.serviceImpl;

import git.darul.internet_banking.dto.response.BankAccountResponse;
import git.darul.internet_banking.dto.response.BankDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BankServiceImpl bankService;

    private String bankCode;
    private String accountNumber;
    private BankAccountResponse mockBankAccountResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankCode = "BCA";
        accountNumber = "1234567890";

        // Create mock BankDataResponse
        BankDataResponse mockBankDataResponse = new BankDataResponse();
        mockBankDataResponse.setBankCode(bankCode);
        mockBankDataResponse.setBankName("Bank Central Asia");
        mockBankDataResponse.setAccountNumber(accountNumber);
        mockBankDataResponse.setAccountName("John Doe");

        // Create mock BankAccountResponse
        mockBankAccountResponse = new BankAccountResponse();
        mockBankAccountResponse.setStatus("success");
        mockBankAccountResponse.setMsg("Account details fetched successfully");
        mockBankAccountResponse.setData(mockBankDataResponse);
    }

    @Test
    void testGetListOfBanks_Success() {
        // Arrange
        String url = "https://api-rekening.lfourr.com/listBank";
        Object mockResponse = new Object();  // Example mock response
        when(restTemplate.getForObject(url, Object.class)).thenReturn(mockResponse);

        // Act
        Object result = bankService.getListOfBanks();

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForObject(url, Object.class);
    }

    @Test
    void testGetListOfEwallets_Success() {
        // Arrange
        String url = "https://api-rekening.lfourr.com/listEwallet";
        Object mockResponse = new Object();  // Example mock response
        when(restTemplate.getForObject(url, Object.class)).thenReturn(mockResponse);

        // Act
        Object result = bankService.getListOfEwallets();

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForObject(url, Object.class);
    }

    @Test
    void testGetBankAccountDetails_Success() {
        // Arrange
        String url = "https://api-rekening.lfourr.com/getBankAccount?bankCode={bankCode}&accountNumber={accountNumber}";
        when(restTemplate.getForObject(url, BankAccountResponse.class, bankCode, accountNumber))
                .thenReturn(mockBankAccountResponse);

        // Act
        BankAccountResponse result = bankService.getBankAccountDetails(bankCode, accountNumber);

        // Assert
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Account details fetched successfully", result.getMsg());
        assertNotNull(result.getData());
        assertEquals(bankCode, result.getData().getBankCode());
        assertEquals(accountNumber, result.getData().getAccountNumber());
        assertEquals("John Doe", result.getData().getAccountName());
        verify(restTemplate, times(1)).getForObject(url, BankAccountResponse.class, bankCode, accountNumber);
    }

    @Test
    void testGetEwalletAccountDetails_Success() {
        // Arrange
        String url = "https://api-rekening.lfourr.com/getEwalletAccount?bankCode={bankCode}&accountNumber={accountNumber}";
        when(restTemplate.getForObject(url, BankAccountResponse.class, bankCode, accountNumber))
                .thenReturn(mockBankAccountResponse);

        // Act
        BankAccountResponse result = bankService.getEwalletAccountDetails(bankCode, accountNumber);

        // Assert
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Account details fetched successfully", result.getMsg());
        assertNotNull(result.getData());
        assertEquals(bankCode, result.getData().getBankCode());
        assertEquals(accountNumber, result.getData().getAccountNumber());
        assertEquals("John Doe", result.getData().getAccountName());
        verify(restTemplate, times(1)).getForObject(url, BankAccountResponse.class, bankCode, accountNumber);
    }

    @Test
    void testGetListOfBanks_Failure() {
        // Arrange
        String url = "https://api-rekening.lfourr.com/listBank";
        when(restTemplate.getForObject(url, Object.class)).thenThrow(new RuntimeException("Error fetching banks"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> bankService.getListOfBanks());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Error fetching banks"));
    }

    @Test
    void testGetBankAccountDetails_Failure() {
        // Arrange
        String url = "https://api-rekening.lfourr.com/getBankAccount?bankCode={bankCode}&accountNumber={accountNumber}";
        when(restTemplate.getForObject(url, BankAccountResponse.class, bankCode, accountNumber))
                .thenThrow(new RuntimeException("Error fetching bank account details"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> bankService.getBankAccountDetails(bankCode, accountNumber));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Error fetching bank account details"));
    }

}
