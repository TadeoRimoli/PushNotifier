package EntrenaYa.PushNotifier.DB;


import EntrenaYa.PushNotifier.Entites.notification.Account;
import EntrenaYa.PushNotifier.Entites.notification.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

        @EntityGraph(attributePaths = "account")
        Optional<RefreshToken> findByToken(String token);
        @EntityGraph(attributePaths = "account")
        Optional<RefreshToken> findByAccount(Account token);

        @Transactional
        void deleteByToken(String token);

        @Transactional
        int deleteByAccount(Account user);
    }

