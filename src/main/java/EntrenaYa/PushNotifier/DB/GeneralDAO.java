package EntrenaYa.PushNotifier.DB;

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



    public Notification saveNotification(Notification notification) {
        // Si el ID es nulo, el objeto es nuevo y se debe persistir
        if (notification.getId() == 0) {
            entityManager.persist(notification);
            return notification;
        } else {
            // Si el ID no es nulo, se debe actualizar
            return entityManager.merge(notification);
        }
    }
}
