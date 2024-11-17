package git.darul.internet_banking.service;

import git.darul.internet_banking.dto.request.TransactionRequest;
import git.darul.internet_banking.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    List<TransactionResponse> getAllTransactions();
    TransactionResponse getTransactionById(String id);
}
