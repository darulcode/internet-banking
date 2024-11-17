package git.darul.mandiritest.service;

import git.darul.mandiritest.dto.response.AccountResponse;
import git.darul.mandiritest.dto.response.GetBalanceResponse;
import git.darul.mandiritest.entity.Account;

public interface AccountService {
    Account getOne(String id);
    Account create(String name);
    AccountResponse getAccountById();
    GetBalanceResponse getBalance(Integer accountNumber);
    void topUpBalance(Long balance);
    void updateBalance(Long balance, Integer accountNumber);
}
