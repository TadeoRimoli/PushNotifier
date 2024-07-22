package EntrenaYa.PushNotifier.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@Log4j2
@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.jwtTokenValiditySeconds}")
    protected Long jwtTokenValiditySeconds;

    @Value("${jwt.jwtRefreshTokenValiditySeconds}")
    protected Long jwtRefreshTokenValiditySeconds;
    public String getUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return getClaimFromToken(token, Claims::getSubject);
    }
    public static final String TOKEN_CLAIM_TYPE = "tokentype";

    public enum EToken{
        NORMAL("normal"),
        REFRESH("refresh");

        private String url;

        EToken(String envUrl) {
            this.url = envUrl;
        }

        public String getUrl() {
            return url;
        }
    }

    public String getClaimFromToken(String token, String key) {
        final Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get(key);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token)
                .getBody();
    }
    public String generateToken(String username,Long userId) {

        Claims claims = Jwts.claims();
        claims.put("userId",String.valueOf(userId));
//        claims.put("profileId",String.valueOf(profileId));
        claims.put(TOKEN_CLAIM_TYPE, EToken.NORMAL);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return _generateToken(username, claims, key,jwtTokenValiditySeconds);
    }
    public String generateRefreshToken(String username,Long userId) {

        Claims claims = Jwts.claims();
        claims.put("userId",String.valueOf(userId));
//        claims.put("profileId",String.valueOf(profileId));
        claims.put(TOKEN_CLAIM_TYPE, EToken.REFRESH);
//        claims.put("token-uuid", UUID.randomUUID().toString());
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return _generateToken(username, claims, key,jwtRefreshTokenValiditySeconds);

    }
    protected String _generateToken(String username, Claims claims, Key key, long expirationSeconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return getClaimFromToken(token, "userId");
    }

    public LocalDateTime getRefreshTokenExpiration(){
        return LocalDateTime.now().plusSeconds(jwtRefreshTokenValiditySeconds);
    }
}
