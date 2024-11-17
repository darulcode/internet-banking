package git.darul.mandiritest.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountResponse {
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "msg")
    private String msg;
    @JsonProperty(value = "data")
    private BankDataResponse data;

}
