package EntrenaYa.PushNotifier.API;

import EntrenaYa.PushNotifier.DB.GeneralDAO;
import EntrenaYa.PushNotifier.DTOs.NotificationDTO;
import EntrenaYa.PushNotifier.DTOs.ResponseDTO;
import EntrenaYa.PushNotifier.Entites.notification.Notification;
import EntrenaYa.PushNotifier.JWT.JwtTokenUtils;
import EntrenaYa.PushNotifier.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @PostMapping("/saveNotification")
    public ResponseEntity<ResponseDTO> createNotification(HttpServletRequest request,@RequestBody NotificationDTO notification) {
        String jwtToken = request.getHeader("Authorization");

        long account = Long.parseLong(jwtTokenUtils.getAccountIdFromToken(jwtToken));

        Notification savedNotification = generalDAO.saveNotification(account,notification);

        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .errormessage("")
                .id(savedNotification.getId())
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/saveNotifications")
    public ResponseEntity<ResponseDTO> createNotifications(HttpServletRequest request,@RequestBody List<NotificationDTO> notifications) {
        List<Notification> savedNotifications = new ArrayList<>();
        String jwtToken = request.getHeader("Authorization");
        long account = Long.parseLong(jwtTokenUtils.getAccountIdFromToken(jwtToken));
        for (NotificationDTO notification : notifications) {
            Notification savedNotification = generalDAO.saveNotification(account,notification);
            savedNotifications.add(savedNotification);
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .errormessage("")
                .object(savedNotifications)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<ResponseDTO> deleteNotificationsByEvent(HttpServletRequest request,@PathVariable Long eventId) {
        String jwtToken = request.getHeader("Authorization");
        long account = Long.parseLong(jwtTokenUtils.getAccountIdFromToken(jwtToken));
        try{
            generalDAO.deleteByEvent(account,eventId);
        }catch (Exception e){
            return ResponseEntity.ok(ResponseDTO.builder().success(false).errormessage(e.getMessage()).build());
        }
        return ResponseEntity.ok(ResponseDTO.builder().success(true).build());
    }
    @DeleteMapping("/deleteEntity/{entity}")
    public ResponseEntity<ResponseDTO> deleteNotificationsByEntity(HttpServletRequest request,@PathVariable Long entity) {
        String jwtToken = request.getHeader("Authorization");
        long account = Long.parseLong(jwtTokenUtils.getAccountIdFromToken(jwtToken));
        try{
            generalDAO.deleteByEntity(account,entity);
        }catch (Exception e){
            return ResponseEntity.ok(ResponseDTO.builder().success(false).errormessage(e.getMessage()).build());
        }
        return ResponseEntity.ok(ResponseDTO.builder().success(true).build());
    }




}
