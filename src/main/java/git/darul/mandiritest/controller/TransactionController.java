package git.darul.mandiritest.controller;


import git.darul.mandiritest.constants.Constants;
import git.darul.mandiritest.dto.request.TransactionRequest;
import git.darul.mandiritest.dto.response.TransactionResponse;
import git.darul.mandiritest.service.TransactionService;
import git.darul.mandiritest.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.TRANSACTION_API)
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.createTransaction(transactionRequest);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constants.SUCCESS_CREATE_TRANSACTION, response);
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        List<TransactionResponse> allTransactions = transactionService.getAllTransactions();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constants.SUCCESS_FETCH_ALL_TRANSACTION, allTransactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constants.SUCCESS_FETCH_TRANSACTION, response);
    }

}
