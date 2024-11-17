package git.darul.mandiritest.repository;

import git.darul.mandiritest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(nativeQuery = true, value = "Select id, account_number, balance, name from m_account where id=:id")
    Optional<Account> findById(String id);

    @Query(nativeQuery = true, value = "Select  * from m_account where account_number=:number")
    Optional<Account> findBalanceByAccountNumber(Integer number);


    @Modifying
    @Query(value = "INSERT INTO m_account (id, account_number, balance, name) VALUES (:id, :accountNumber, :balance, :name)", nativeQuery = true)
    void insertAccount(
            @Param("id") String id,
            @Param("accountNumber") Integer accountNumber,
                       @Param("balance") Long balance,
                       @Param("name") String name);

    @Query(value = "SELECT id FROM m_account WHERE account_number = :accountNumber", nativeQuery = true)
    String findIdByAccountNumber(@Param("accountNumber") Integer accountNumber);


    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "UPDATE m_account SET balance =balance +  :balance WHERE id = :accountId", nativeQuery = true)
    void updateBalance(@Param("balance") Long balance, @Param("accountId") String accountID);

    @Modifying
    @Transactional (rollbackFor = Exception.class)
    @Query(value = "UPDATE m_account SET balance =balance +  :balance WHERE account_number = :accountNumber", nativeQuery = true)
    void updateBalanceOnTransaction(@Param("balance") Long balance, @Param("accountNumber") Integer accountNumber);
}
