package git.darul.internet_banking.service.serviceImpl;

import git.darul.internet_banking.constants.Constants;
import git.darul.internet_banking.dto.response.AccountResponse;
import git.darul.internet_banking.dto.response.GetBalanceResponse;
import git.darul.internet_banking.entity.Account;
import git.darul.internet_banking.entity.User;
import git.darul.internet_banking.repository.AccountRepository;
import git.darul.internet_banking.service.AccountService;
import git.darul.internet_banking.util.AuthenticationContextUtil;
import git.darul.internet_banking.util.GenerateAccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getOne(String id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account with id " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Account create(String name) {
        Integer accountNumber = GenerateAccountNumber.generateAccountNumber();
        String uuid = String.valueOf(UUID.randomUUID());
        accountRepository.insertAccount(uuid, accountNumber, 0L, name);
        String id = accountRepository.findIdByAccountNumber(accountNumber);

        Account account = Account.builder()
                .id(id)
                .name(name)
                .balance(0L)
                .accountNumber(accountNumber)
                .build();

        return account;
    }

    @Override
    public AccountResponse getAccountById() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        return getAccountResponse(getOne(currentUser.getAccount().getId()));
    }

    @Override
    public GetBalanceResponse getBalance(Integer accountNumber) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findBalanceByAccountNumber(accountNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.FAILED_FETCH_BALANCE)));
        return GetBalanceResponse.builder()
                .id(account.get().getId())
                .name(account.get().getName())
                .accountNumber(account.get().getAccountNumber())
                .balance(account.get().getBalance())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void topUpBalance(Long balance) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        accountRepository.updateBalance(balance, currentUser.getAccount().getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBalance(Long balance, Integer accountNumber) {
        accountRepository.updateBalanceOnTransaction(balance, accountNumber);
    }

    private AccountResponse getAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .name(account.getName())
                .balance(account.getBalance())
                .build();
    }
}
