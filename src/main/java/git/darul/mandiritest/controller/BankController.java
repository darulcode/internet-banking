package git.darul.mandiritest.controller;


import git.darul.mandiritest.dto.request.BankEwalletRequest;
import git.darul.mandiritest.dto.response.BankAccountResponse;
import git.darul.mandiritest.dto.response.CommonResponse;
import git.darul.mandiritest.dto.response.ListBankResponse;
import git.darul.mandiritest.service.BankService;
import git.darul.mandiritest.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BankController {

    private static final Logger log = LoggerFactory.getLogger(BankController.class);
    private final BankService apiService;

    @Operation(
            summary = "Get list of banks",
            description = "Fetches a list of all available banks from the external service",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                            content = @Content(schema = @Schema(implementation = ListBankResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/banks")
    public ResponseEntity<?> getListOfBanks() {
        Object listOfBanks = apiService.getListOfBanks();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success fetch banks", listOfBanks);
    }

    @Operation(
            summary = "Get list of ewallet",
            description = "Fetches a list of all available ewallet from the external service",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                            content = @Content(schema = @Schema(implementation = ListBankResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/ewallets")
    public ResponseEntity<?> getListOfEwallets() {
        Object listOfEwallets = apiService.getListOfEwallets();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success fetch ewallet", listOfEwallets);
    }

    @Operation(
            summary = "Get bank account detail",
            description = "Fetches a list of all available banks from the external service",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                            content = @Content(schema = @Schema(implementation = BankAccountResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/bankAccount")
    public ResponseEntity<?> getBankAccountDetails(@RequestParam String bankCode,@RequestParam String bankNumber) {
        Object bankAccountDetails = apiService.getBankAccountDetails(bankCode, bankNumber);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success fetch banks", bankAccountDetails);
    }

    @Operation(
            summary = "Get ewallet detail",
            description = "Fetches a list of all available ewallet from the external service",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                            content = @Content(schema = @Schema(implementation = BankAccountResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/ewalletAccount")
    public ResponseEntity<?> getEwalletAccountDetails(@RequestParam String bankCode,@RequestParam String bankNumber) {
        Object ewalletAccountDetails = apiService.getEwalletAccountDetails(bankCode, bankNumber);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success fetch banks", ewalletAccountDetails);
    }
}
