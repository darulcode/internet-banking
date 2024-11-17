package git.darul.internet_banking.dto.response;

import git.darul.internet_banking.dto.request.Bank;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListBankResponse {
    private List<Bank> banks;
}