package git.darul.internet_banking.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBalanceResponse {
    private String id;
    private String name;
    private Integer accountNumber;
    private Long balance;

}
