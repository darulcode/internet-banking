package git.darul.internet_banking.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String username;
    private Integer accountNumber;
}
