package EntrenaYa.PushNotifier.Entites.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long refreshtoken;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "account", referencedColumnName = "id")
    protected Account account;
    protected String token;
    protected LocalDateTime expiration;
}

