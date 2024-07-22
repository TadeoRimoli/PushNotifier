package EntrenaYa.PushNotifier.DB;

import EntrenaYa.PushNotifier.Entites.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}