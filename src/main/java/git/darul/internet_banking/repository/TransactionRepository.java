package git.darul.internet_banking.repository;

import git.darul.internet_banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query(nativeQuery = true, value = "Select * from t_transactions WHERE receive_account = :account OR send_account=:account")
    List<Transaction> findAllTransactionsByAccount(String account);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO t_transactions (id, amount, date, send_account, receive_account, status, bank, bank_number) VALUES (:id, :amount, :date, :sendAccount, :receiveAccount, :status, :bank, :accountNumber)")
    void insertTransaction(String id, Long amount, LocalDateTime date, String sendAccount, String receiveAccount, String status, String bank, String accountNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM t_transactions WHERE (id=:id ) AND (receive_account = :account OR send_account=:account)")
    Optional<Transaction> getById(String id, String account);
}
