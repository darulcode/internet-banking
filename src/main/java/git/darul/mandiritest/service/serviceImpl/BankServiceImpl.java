package git.darul.mandiritest.service.serviceImpl;

import git.darul.mandiritest.dto.response.BankAccountResponse;
import git.darul.mandiritest.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {

    private final RestTemplate restTemplate;

    @Override
    public Object getListOfBanks() {
        try {
            log.info("Get List of Banks");
            String url = "https://api-rekening.lfourr.com/listBank";
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            // Log error or handle it as needed
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Object getListOfEwallets() {
        try {
            String url = "https://api-rekening.lfourr.com/listEwallet";
            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            // Log error or handle it as needed
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public BankAccountResponse getBankAccountDetails(String bankCode, String accountNumber) {
        try {
            String url = "https://api-rekening.lfourr.com/getBankAccount?bankCode={bankCode}&accountNumber={accountNumber}";
            return restTemplate.getForObject(url, BankAccountResponse.class, bankCode, accountNumber);
        } catch (Exception e) {
            // Log error or handle it as needed
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public BankAccountResponse getEwalletAccountDetails(String bankCode, String accountNumber) {
        try {
            String url = "https://api-rekening.lfourr.com/getEwalletAccount?bankCode={bankCode}&accountNumber={accountNumber}";
            return restTemplate.getForObject(url, BankAccountResponse.class, bankCode, accountNumber);
        } catch (Exception e) {
            // Log error or handle it as needed
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
