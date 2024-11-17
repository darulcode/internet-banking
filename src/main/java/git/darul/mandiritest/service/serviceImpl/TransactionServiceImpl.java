package git.darul.mandiritest.service.serviceImpl;

import git.darul.mandiritest.constants.Constants;
import git.darul.mandiritest.constants.StatusTransaction;
import git.darul.mandiritest.dto.request.TransactionRequest;
import git.darul.mandiritest.dto.response.BankAccountResponse;
import git.darul.mandiritest.dto.response.GetBalanceResponse;
import git.darul.mandiritest.dto.response.TransactionResponse;
import git.darul.mandiritest.entity.Account;
import git.darul.mandiritest.entity.Transaction;
import git.darul.mandiritest.entity.User;
import git.darul.mandiritest.repository.TransactionRepository;
import git.darul.mandiritest.service.AccountService;
import git.darul.mandiritest.service.BankService;
import git.darul.mandiritest.service.TransactionService;
import git.darul.mandiritest.util.AuthenticationContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final BankService bankService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        Account senderAccount = accountService.getOne(currentUser.getAccount().getId());

        validateBalance(senderAccount, transactionRequest.getAmount());

        String transactionId = UUID.randomUUID().toString();
        accountService.updateBalance(-transactionRequest.getAmount(), senderAccount.getAccountNumber());

        String receiverName;
        String receiverAccountNumber;

        if (!"DARUL".equalsIgnoreCase(transactionRequest.getBank())) {
            // Handle external bank transaction
            BankAccountResponse bankAccountDetails = validateExternalBankAccount(transactionRequest);
            receiverName = bankAccountDetails.getData().getAccountName();
            receiverAccountNumber = bankAccountDetails.getData().getAccountNumber();
            insertExternalTransaction(transactionId, transactionRequest, senderAccount, bankAccountDetails);
        } else {
            // Handle internal transaction
            GetBalanceResponse receiverAccount = validateInternalAccount(transactionRequest);
            accountService.updateBalance(transactionRequest.getAmount(), receiverAccount.getAccountNumber());
            receiverName = receiverAccount.getName();
            receiverAccountNumber = String.valueOf(receiverAccount.getAccountNumber());
            insertInternalTransaction(transactionId, transactionRequest, senderAccount, receiverAccount);
        }

        return buildTransactionResponse(transactionId, transactionRequest, senderAccount, receiverName, receiverAccountNumber);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findAllTransactionsByAccount(currentUser.getAccount().getId());
        return transactions.stream()
                .map(this::buildTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionById(String id) {
        User currentUser = AuthenticationContextUtil.getCurrentUser();
        Transaction transaction = transactionRepository.getById(id, currentUser.getAccount().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.NOTFOUND_TRANSACTION));
        return buildTransactionResponse(transaction);
    }

    private void validateBalance(Account account, double amount) {
        if (account.getBalance() < amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.BALANCE_LOW);
        }
    }

    private BankAccountResponse validateExternalBankAccount(TransactionRequest request) {
        BankAccountResponse bankAccountDetails = bankService.getBankAccountDetails(request.getBank(), request.getToAccountNumber().toString());
        if (!Boolean.parseBoolean(bankAccountDetails.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.NOTFOUND_BANK_ACCOUNT);
        }
        return bankAccountDetails;
    }

    private GetBalanceResponse validateInternalAccount(TransactionRequest request) {
        GetBalanceResponse accountResponse = accountService.getBalance((int) Long.parseLong(request.getToAccountNumber()));
        if (accountResponse == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.NOTFOUND_BANK_ACCOUNT);
        }
        return accountResponse;
    }

    private void insertExternalTransaction(String id, TransactionRequest request, Account sender, BankAccountResponse bankDetails) {
        transactionRepository.insertTransaction(
                id,
                request.getAmount(),
                LocalDateTime.now(),
                sender.getId(),
                null,
                StatusTransaction.COMPLETED.toString(),
                request.getBank(),
                bankDetails.getData().getAccountNumber()
        );
    }

    private void insertInternalTransaction(String id, TransactionRequest request, Account sender, GetBalanceResponse receiver) {
        transactionRepository.insertTransaction(
                id,
                request.getAmount(),
                LocalDateTime.now(),
                sender.getId(),
                receiver.getId(),
                StatusTransaction.COMPLETED.toString(),
                request.getBank(),
                null
        );
    }

    private TransactionResponse buildTransactionResponse(String id, TransactionRequest request, Account sender, String receiverName, String receiverAccountNumber) {
        return TransactionResponse.builder()
                .transactionID(id)
                .amount(request.getAmount())
                .statusTransaction(StatusTransaction.COMPLETED.getDescription())
                .sendAccountName(sender.getName())
                .sendAccountNumber(sender.getAccountNumber())
                .receiveAccountName(receiverName)
                .receiveAccountNumber(receiverAccountNumber)
                .bank(request.getBank())
                .build();
    }

    private TransactionResponse buildTransactionResponse(Transaction transaction) {
        Account sender = accountService.getOne(transaction.getSendAccount().getId());
        Account receiver = transaction.getReceiveAccount() != null ? accountService.getOne(transaction.getReceiveAccount().getId()) : null;
        return TransactionResponse.builder()
                .transactionID(transaction.getId())
                .amount(transaction.getAmount())
                .statusTransaction(transaction.getStatus().getDescription())
                .sendAccountName(sender.getName())
                .sendAccountNumber(sender.getAccountNumber())
                .receiveAccountName(receiver != null ? receiver.getName() : null)
                .receiveAccountNumber(String.valueOf(receiver != null ? receiver.getAccountNumber() : null))
                .build();
    }
}
