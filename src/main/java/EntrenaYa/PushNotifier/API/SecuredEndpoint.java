package EntrenaYa.PushNotifier.API;

import EntrenaYa.PushNotifier.DB.GeneralDAO;
import EntrenaYa.PushNotifier.DTOs.ResponseDTO;
import EntrenaYa.PushNotifier.Entites.notification.Notification;
import EntrenaYa.PushNotifier.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<ResponseDTO> createNotification(@RequestBody Notification notification) {


        // Persistir la notificación en la base de datos
        Notification savedNotification = generalDAO.saveNotification(notification);
        // Calcular el delay en milisegundos

        // Crear y devolver la respuesta
        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .errormessage("")
                .id(savedNotification.getId())
                .object(savedNotification)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
