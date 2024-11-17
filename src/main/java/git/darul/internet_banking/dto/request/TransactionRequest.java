package git.darul.internet_banking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionRequest {
    private String toAccountNumber;
    private Long amount;
    private String bank;
}
