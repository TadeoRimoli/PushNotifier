package EntrenaYa.PushNotifier.DB;

import EntrenaYa.PushNotifier.Entites.notification.Account;
import EntrenaYa.PushNotifier.Entites.notification.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Log4j2
@Service
public class JWTservice {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(Account account, String token,
                                           LocalDateTime expiration) {
        refreshTokenRepository.deleteByAccount(account);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(account);
        refreshToken.setToken(token);
        refreshToken.setExpiration(expiration);
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public Optional<RefreshToken> findByAccount(Account account) {
        return refreshTokenRepository.findByAccount(account);
    }
    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
