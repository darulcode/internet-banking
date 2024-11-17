package git.darul.internet_banking.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String name;

}
