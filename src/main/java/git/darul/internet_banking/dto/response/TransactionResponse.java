package git.darul.internet_banking.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String transactionID;
    private Long amount;
    private String bank;
    private String sendAccountName;
    private Integer sendAccountNumber;
    private String receiveAccountName;
    private String receiveAccountNumber;
    private String statusTransaction;

}
