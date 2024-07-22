package EntrenaYa.PushNotifier.API;

import EntrenaYa.PushNotifier.DB.GeneralDAO;
import EntrenaYa.PushNotifier.DB.JWTservice;
import EntrenaYa.PushNotifier.DTOs.ResponseDTO;
import EntrenaYa.PushNotifier.Entites.notification.Account;
import EntrenaYa.PushNotifier.Entites.notification.RefreshToken;
import EntrenaYa.PushNotifier.JWT.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notsecured/")
@Log4j2
public class Endpoint {
    @Autowired
    private GeneralDAO generalDAO;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTservice jwtService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginin(@RequestParam String accountName, @RequestParam String password) {
        Account account = generalDAO.findAccountByName(accountName);

        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            // Generate a new access token and refresh token
            String accessToken = jwtTokenUtils.generateToken(accountName, account.getId());
            String refreshToken = jwtTokenUtils.generateRefreshToken(accountName, account.getId());

            // Save the refresh token using JWTservice
            jwtService.createRefreshToken(account, refreshToken, jwtTokenUtils.getRefreshTokenExpiration());
            // Return the tokens in the response
            return ResponseEntity.ok(ResponseDTO.builder()
                    .success(true)
                    .object(Map.of("accessToken", accessToken, "refreshToken", refreshToken))
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.builder()
                            .success(false)
                            .errormessage("Invalid account name or password")
                            .build());
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDTO> refreshToken(@RequestParam String refreshToken) {
        Optional<RefreshToken> storedTokenOpt = jwtService.findByToken(refreshToken);

        if (storedTokenOpt.isPresent() && storedTokenOpt.get().getExpiration().isAfter(LocalDateTime.now())) {
            RefreshToken storedToken = storedTokenOpt.get();

            // Generate a new access token and refresh token
            String newAccessToken = jwtTokenUtils.generateToken(storedToken.getAccount().getAccountname(), storedToken.getAccount().getId());
            String newRefreshToken = jwtTokenUtils.generateRefreshToken(storedToken.getAccount().getAccountname(), storedToken.getAccount().getId());

            // Save the new refresh token and delete the old one
            jwtService.createRefreshToken(storedToken.getAccount(), newRefreshToken, jwtTokenUtils.getRefreshTokenExpiration());
            jwtService.deleteByToken(refreshToken);

            // Return the new tokens in the response
            return ResponseEntity.ok(ResponseDTO.builder()
                    .success(true)
                    .object(Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken))
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.builder()
                            .success(false)
                            .errormessage("Invalid or expired refresh token")
                            .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestParam String accountName, @RequestParam String password) {
        if (generalDAO.findAccountByName(accountName) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.builder()
                            .success(false)
                            .errormessage("Account already exists")
                            .build());
        }

        Account account = new Account();
        account.setAccountname(accountName);
        account.setPassword(passwordEncoder.encode(password));
        generalDAO.saveAccount(account);

        return ResponseEntity.ok(ResponseDTO.builder()
                .success(true)
                .build());
    }


}
