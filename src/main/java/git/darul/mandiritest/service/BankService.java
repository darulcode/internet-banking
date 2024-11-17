package git.darul.mandiritest.service;

import git.darul.mandiritest.dto.request.BankEwalletRequest;
import git.darul.mandiritest.dto.response.BankAccountResponse;

public interface BankService {
    Object getListOfBanks();
    Object getListOfEwallets();
    BankAccountResponse getBankAccountDetails(String bankCode, String bankNumber);
    BankAccountResponse getEwalletAccountDetails(String bankCode, String bankNumber);
}
