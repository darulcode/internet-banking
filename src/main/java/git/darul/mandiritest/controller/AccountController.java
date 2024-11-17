package git.darul.mandiritest.controller;


import git.darul.mandiritest.constants.Constants;
import git.darul.mandiritest.dto.request.TopUpBalanceRequest;
import git.darul.mandiritest.dto.response.AccountResponse;
import git.darul.mandiritest.dto.response.GetBalanceResponse;
import git.darul.mandiritest.service.AccountService;
import git.darul.mandiritest.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ACCOUNT_API)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<?> getAccount() {
        AccountResponse response = accountService.getAccountById();
        return ResponseUtil.buildResponse(HttpStatus.OK, Constants.SUCCESS_FETCH_ACCOUNT, response);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<?> getBalance(@PathVariable Integer accountNumber) {
        GetBalanceResponse balance = accountService.getBalance(accountNumber);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constants.SUCCESS_FETCH_BALANCE, balance);
    }

    @PutMapping("/balance")
    public ResponseEntity<?> topUpBalance(@RequestBody TopUpBalanceRequest request) {
        accountService.topUpBalance(request.getBalance());
        return ResponseUtil.buildResponse(HttpStatus.OK, Constants.SUCCESS_UPDATE_BALANCE, null);
    }

}
