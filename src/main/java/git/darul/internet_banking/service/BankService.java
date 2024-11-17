package git.darul.internet_banking.service;

import git.darul.internet_banking.dto.response.BankAccountResponse;

public interface BankService {
    Object getListOfBanks();
    Object getListOfEwallets();
    BankAccountResponse getBankAccountDetails(String bankCode, String bankNumber);
    BankAccountResponse getEwalletAccountDetails(String bankCode, String bankNumber);
}
