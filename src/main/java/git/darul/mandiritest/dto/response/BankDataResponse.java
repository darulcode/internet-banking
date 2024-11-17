package git.darul.mandiritest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDataResponse {
    @JsonProperty(value = "bankcode")
    private String bankCode;
    @JsonProperty(value = "bankname")
    private String bankName;
    @JsonProperty(value = "accountnumber")
    private String accountNumber;
    @JsonProperty(value = "accountname")
    private String accountName;
}
