package EntrenaYa.PushNotifier.API;

import EntrenaYa.PushNotifier.DB.GeneralDAO;
import EntrenaYa.PushNotifier.DTOs.NotificationDTO;
import EntrenaYa.PushNotifier.DTOs.ResponseDTO;
import EntrenaYa.PushNotifier.Entites.notification.Notification;
import EntrenaYa.PushNotifier.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/secured/")
public class SecuredEndpoint {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private GeneralDAO generalDAO;

    @PostMapping("/saveNotification")
    public ResponseEntity<ResponseDTO> createNotification(@RequestBody NotificationDTO notification) {

        Notification savedNotification = generalDAO.saveNotification(notification);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .errormessage("")
                .id(savedNotification.getId())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/saveNotifications")
    public ResponseEntity<ResponseDTO> createNotifications(@RequestBody List<NotificationDTO> notifications) {
        List<Notification> savedNotifications = new ArrayList<>();

        for (NotificationDTO notification : notifications) {
            Notification savedNotification = generalDAO.saveNotification(notification);
            savedNotifications.add(savedNotification);
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .errormessage("")
                .object(savedNotifications)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
