package git.darul.internet_banking.entity;

import git.darul.internet_banking.constants.Constants;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = Constants.TABLE_ACCOUNT)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "account_number",unique = true, nullable = false)
    private Integer accountNumber;

    @Column(name = "balance", nullable = false, columnDefinition = "bigint check (balance >= 0)")
    private Long balance;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "sendAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

}
