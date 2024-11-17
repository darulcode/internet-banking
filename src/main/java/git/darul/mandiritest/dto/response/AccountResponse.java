package git.darul.mandiritest.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountResponse {
    private String id;
    private String name;
    private Integer accountNumber;
    private Long balance;
}
