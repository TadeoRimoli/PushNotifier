package EntrenaYa.PushNotifier.Entites.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    private String title;
    private String message;
    @Column(name = "expotoken")
    private String expoToken;
    @Column(name = "datetime")
    private LocalDateTime dateTime;
    @Column(name = "multipleusers")
    private boolean multipleUsers;
    private Long user;
    private Long event;
    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;
    private Long entity;

}
