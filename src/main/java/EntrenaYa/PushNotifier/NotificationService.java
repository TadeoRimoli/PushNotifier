package EntrenaYa.PushNotifier;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
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
        String url = "https://api.expo.dev/v2/push/send?useFcmV1=true";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String payload = String.format("{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\"}", expoToken, title, message);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        int maxAttempts = 3;
        int attempt = 0;
        boolean sentSuccessfully = false;

        while (attempt < maxAttempts && !sentSuccessfully) {
            attempt++;
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    // Log response or handle success
                    System.out.println("Notification sent successfully: " + response.getBody());
                    sentSuccessfully = true;
                } else {
                    // Log response or handle failure
                    System.out.println("Failed to send notification, attempt " + attempt + ": " + response.getBody());
                }
            } catch (Exception e) {
                // Log exception
                System.out.println("Exception occurred on attempt " + attempt + ": " + e.getMessage());
            }
        }

        if (!sentSuccessfully) {
            System.out.println("Failed to send notification after " + maxAttempts + " attempts.");
        }
    }
}
