package EntrenaYa.PushNotifier.DB;

import EntrenaYa.PushNotifier.Entites.notification.Notification;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
public class NotificationSchedulerService {

    @Autowired
    private NotificationRepository notificationRepository;
    private static final String PUSH_NOTIFICATION_URL = "https://exp.host/--/api/v2/push/send";
    private RestTemplate restTemplate = new RestTemplate();
    @Scheduled(cron = "0 * * * * *") // Ejecuta cada minuto
    public void checkAndSendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(1);

        // Consultar notificaciones para los próximos 1 minutos
        List<Notification> notificationsToSend = notificationRepository.findByDateTimeBetween(now, endTime);

        for (Notification notification : notificationsToSend) {
            if(notification.getGetTokensUrl().isEmpty())
                sendPushNotification(notification);
            else{
                List<String> expoPushTokens = getTokensFromUrl(notification.getGetTokensUrl()+"?groupId="+notification.getUser());
                sendPushNotifications(notification,expoPushTokens);
            }
            //  eliminar la notificación o marcarla como enviada
            notificationRepository.delete(notification);
            log.info("noti enviada");

        }
    }
    private List<String> getTokensFromUrl(String url) {
        return restTemplate.getForObject(url, List.class);
    }
    private void sendPushNotification(Notification notification) {
        // Crear el cuerpo de la notificación
        String requestBody = String.format(
                "{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\"}",
                notification.getExpoToken(),
                notification.getTitle(),
                notification.getMessage()
        );

        // Enviar la notificación
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(PUSH_NOTIFICATION_URL, requestBody, String.class);
        } catch (Exception e) {
            // Manejar errores, como logs
            e.printStackTrace();
        }
    }
    public void sendPushNotifications(Notification notification, List<String> expoPushTokens) {
        for (String token : expoPushTokens) {
            // Crear el cuerpo de la notificación
            String requestBody = String.format(
                    "{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\"}",
                    token,
                    notification.getTitle(),
                    notification.getMessage()
            );

            // Enviar la notificación
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForObject(PUSH_NOTIFICATION_URL, requestBody, String.class);
            } catch (Exception e) {
                // Manejar errores, como logs
                e.printStackTrace();
            }
        }
    }
}

