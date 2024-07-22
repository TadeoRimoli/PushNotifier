package EntrenaYa.PushNotifier;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class NotificationService {

    public void sendPushNotification(String expoToken, String title, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://exp.host/--/api/v2/push/send";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String payload = String.format("{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\"}", expoToken, title, message);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // Log response or handle errors
        System.out.println(response.getBody());
    }

//    @Autowired
//    private EntityManager entityManager;
//    public void sendNotifications() {
//        LocalDateTime endTime = LocalDateTime.now();
//        String sqlQuery = "SELECT * FROM reminder WHERE send=false and datetime > NOW() AND datetime <= DATE_ADD(NOW(), INTERVAL 60 MINUTE)";
//        List<Reminder> reminders = entityManager.createNativeQuery(sqlQuery, Reminder.class).getResultList();
//        if(reminders!=null && !reminders.isEmpty()){
//            reminders.forEach(e->{
//                sendPushNotification(e.getFcmtoken(),e.getTitle(),e.getMessage());
//            });
//        }
//        reminders.forEach(e->{
//            e.setSend(true);
//            entityManager.merge(e);
//        });
//    }
//
//    public void sendPushNotification(
//            String toToken,
//            String title,
//            String body) {
//
//        // URL de la API de Expo para enviar notificaciones
//        String expoApiUrl = "https://exp.host/--/api/v2/push/send";
//        // Construye el objeto de notificación
//        String notificationJson = String.format("{\"to\": \"%s\", \"title\": \"%s\", \"body\": \"%s\"}", toToken, title, body);
//        // Configura los encabezados HTTP
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        // Construye la entidad HTTP con el cuerpo de la solicitud y los encabezados
//        HttpEntity<String> requestEntity = new HttpEntity<>(notificationJson, headers);
//        // Crea una instancia de RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//        int maxRetries = 2;
//        boolean success = false;
//        int retry = 0;
//        while (retry <= maxRetries && !success) {
//            try {
//                // Envía la solicitud POST
//                ResponseEntity<String> response = restTemplate.exchange(expoApiUrl, HttpMethod.POST, requestEntity, String.class);
//                // Verifica si la solicitud fue exitosa
//                if (response.getStatusCode() == HttpStatus.OK) {
//                    success = true; // La solicitud fue exitosa, sal del bucle
//                }
//            } catch (Exception e) {
//                log.error("Error al enviar la notificación (Intento " + retry + "): " + e.getMessage(), e);
//            }
//
//            // Si no se pudo enviar la notificación y aún hay intentos disponibles, espera un tiempo antes de reintentar (puedes ajustar el tiempo de espera según tus necesidades)
//            if (!success && retry < maxRetries) {
//                try {
//                    Thread.sleep(2000); // Espera 1 segundo antes de reintentar
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//
//            retry++;
//        }
//
//    }
}
