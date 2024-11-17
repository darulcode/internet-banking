package git.darul.mandiritest.entity;

import git.darul.mandiritest.constants.Constants;
import git.darul.mandiritest.constants.StatusTransaction;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = Constants.TABLE_TRANSACTIONS)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @CreatedDate
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "send_account", nullable = false)
    private Account sendAccount;

    @ManyToOne
    @JoinColumn(name = "receive_account")
    private Account receiveAccount;

    @Column(name = "bank_number")
    private String bankNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusTransaction status;

    @Column(name = "bank" , nullable = false)
    private String bank;
}
