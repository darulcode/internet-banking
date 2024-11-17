package git.darul.mandiritest.dto.response;

import git.darul.mandiritest.dto.request.Bank;
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