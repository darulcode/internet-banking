package git.darul.mandiritest.repository;

import git.darul.mandiritest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(nativeQuery = true, value = "SELECT  * FROM m_user WHERE username = :username ")
    Optional<User> findByUsername(String username);
    @Query(nativeQuery = true, value = "SELECT  * FROM m_user WHERE id = :id ")
    Optional<User> findById(String id);
    @Query(nativeQuery = true, value = "SELECT * FROM m_user WHERE email=:email OR username=:username LIMIT 1")
    Optional<User> findAllByUser(String email, String username);

    @Modifying
    @Query(value = "INSERT INTO m_user (id, user_account_id, username, password, email) VALUES (:id, :accountId, :username, :password, :email)", nativeQuery = true)
    void insertUser(
            String id,
            String accountId,
                    String username,
                    String password,
                    String email);

}
