package EntrenaYa.PushNotifier.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private long id;
    private String title;
    private String message;
    private LocalDateTime dateTime;
    private boolean multipleUsers;
    private String getTokensUrl;
    private Long user;
    private Long event;
    private Long entity;

}
