package EntrenaYa.PushNotifier.DB;

import EntrenaYa.PushNotifier.DTOs.NotificationDTO;
import EntrenaYa.PushNotifier.Entites.notification.Account;
import EntrenaYa.PushNotifier.Entites.notification.Notification;
import EntrenaYa.PushNotifier.Entites.notification.RefreshToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@Slf4j
public class GeneralDAO {
    @PersistenceContext
    private EntityManager entityManager;

    // **Account Methods**

    public Account saveAccount(Account account) {
        entityManager.persist(account);
        return account;
    }

    public Account findAccountById(long id) {
        return entityManager.find(Account.class, id);
    }

    public Account findAccountByName(String accountName) {
        try {
            String jpql = "SELECT a FROM Account a WHERE a.accountname = :accountName";
            return entityManager.createQuery(jpql, Account.class)
                    .setParameter("accountName", accountName)
                    .getSingleResult();
        } catch (NoResultException e) {
            // No result found, returning null
            return null;
        }
    }

    public void deleteAccount(long id) {
        Account account = findAccountById(id);
        if (account != null) {
            entityManager.remove(account);
        }
    }

    // **Notification Methods**



    public Notification findNotificationById(long id) {
        return entityManager.find(Notification.class, id);
    }

    public List<Notification> findNotificationsByExpoToken(String expoToken) {
       try{
           String jpql = "SELECT n FROM Notification n WHERE n.expoToken = :expoToken";
            return entityManager.createQuery(jpql, Notification.class)
                .setParameter("expoToken", expoToken)
                .getResultList();
        } catch (NoResultException e) {
            // No result found, returning null
            return null;
        }
    }

    public void deleteNotification(long id) {
        Notification notification = findNotificationById(id);
        if (notification != null) {
            entityManager.remove(notification);
        }
    }

    // **RefreshToken Methods**

    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        entityManager.persist(refreshToken);
        return refreshToken;
    }

    public RefreshToken findRefreshTokenById(long id) {
        return entityManager.find(RefreshToken.class, id);
    }

    public RefreshToken findRefreshTokenByToken(String token) {
     try{   String jpql = "SELECT r FROM RefreshToken r WHERE r.token = :token";
        return entityManager.createQuery(jpql, RefreshToken.class)
                .setParameter("token", token)
                .getSingleResult();
    } catch (NoResultException e) {
        // No result found, returning null
        return null;
    }
    }

    public void deleteRefreshToken(long id) {
        RefreshToken refreshToken = findRefreshTokenById(id);
        if (refreshToken != null) {
            entityManager.remove(refreshToken);
        }
    }
    @Transactional
    public Notification saveNotification(long account,NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setExpoToken(notificationDTO.getExpoToken());
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setUser(notificationDTO.getUser());
        notification.setEvent(notificationDTO.getEvent());
        notification.setEntity(notificationDTO.getEntity());
        notification.setMultipleUsers(notificationDTO.isMultipleUsers());
        notification.setDateTime(notificationDTO.getDateTime());
        notification.setAccount(entityManager.find(Account.class,account));
        entityManager.persist(notification);
        return notification;
    }

    @Transactional
    public void deleteByEvent(long accountId, long eventId) {
        Account account = entityManager.find(Account.class, accountId);
        if (account != null) {
            int deletedCount = entityManager.createQuery("DELETE FROM Notification n WHERE n.event = :eventId AND n.account = :account")
                    .setParameter("eventId", eventId)
                    .setParameter("account", account)
                    .executeUpdate();
            System.out.println("Number of notifications deleted by event: " + deletedCount);
        } else {
            System.out.println("Account not found for ID: " + accountId);
        }
    }
    @Transactional
    public void deleteByEntity(long accountId, long entityId) {
        Account account = entityManager.find(Account.class, accountId);
        if (account != null) {
            int deletedCount = entityManager.createQuery("DELETE FROM Notification n WHERE n.entity = :entityId AND n.account = :account")
                    .setParameter("entityId", entityId)
                    .setParameter("account", account)
                    .executeUpdate();
            System.out.println("Number of notifications deleted by entity: " + deletedCount);
        } else {
            System.out.println("Account not found for ID: " + accountId);
        }
    }

    @Transactional
    public void deleteByUser(long accountId, long userId) {
        Account account = entityManager.find(Account.class, accountId);
        if (account != null) {
            int deletedCount = entityManager.createQuery("DELETE FROM Notification n WHERE n.user = :userId AND n.account = :account")
                    .setParameter("userId", userId)
                    .setParameter("account", account)
                    .executeUpdate();
            System.out.println("Number of notifications deleted by user: " + deletedCount);
        } else {
            System.out.println("Account not found for ID: " + accountId);
        }
    }



}
