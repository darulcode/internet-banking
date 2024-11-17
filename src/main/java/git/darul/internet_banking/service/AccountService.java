package git.darul.internet_banking.service;

import git.darul.internet_banking.dto.response.AccountResponse;
import git.darul.internet_banking.dto.response.GetBalanceResponse;
import git.darul.internet_banking.entity.Account;

public interface AccountService {
    Account getOne(String id);
    Account create(String name);
    AccountResponse getAccountById();
    GetBalanceResponse getBalance(Integer accountNumber);
    void topUpBalance(Long balance);
    void updateBalance(Long balance, Integer accountNumber);
}
