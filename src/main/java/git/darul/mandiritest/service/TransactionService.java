package git.darul.mandiritest.service;

import git.darul.mandiritest.dto.request.TransactionRequest;
import git.darul.mandiritest.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    List<TransactionResponse> getAllTransactions();
    TransactionResponse getTransactionById(String id);
}
