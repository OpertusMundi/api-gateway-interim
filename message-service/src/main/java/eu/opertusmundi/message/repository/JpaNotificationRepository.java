package eu.opertusmundi.message.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.opertusmundi.message.domain.NotificationEntity;
import eu.opertusmundi.message.model.NotificationCommandDto;
import eu.opertusmundi.message.model.NotificationDto;

@Repository
@Transactional(readOnly = true)
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    Optional<NotificationEntity> findOneByKey(UUID key);

    @Query("SELECT n FROM Notification n WHERE n.recipient = :recipient")
    List<NotificationEntity> findUserNotifications(@Param("recipient") UUID recipient, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.recipient = :recipient and n.read = false")
    List<NotificationEntity> findUserUnreadNotifications(@Param("recipient") UUID recipient, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE " +
           "   (n.recipient = :userKey) AND " +
           "   (CAST(:dateFrom AS date) IS NULL OR n.sendAt >= :dateFrom) AND " +
           "   (CAST(:dateTo AS date) IS NULL OR n.sendAt <= :dateTo) AND " +
           "   (:read IS NULL OR n.read = :read)")
     Page<NotificationEntity> findAll(
         @Param("userKey") UUID userKey,
         @Param("dateFrom") ZonedDateTime dateFrom,
         @Param("dateTo") ZonedDateTime dateTo,
         @Param("read") Boolean read,
         Pageable pageable
     );

    @Transactional(readOnly = false)
    default NotificationDto send(NotificationCommandDto command) {
        final NotificationEntity notification = new NotificationEntity(command.getRecipient());

        notification.setText(command.getText());

        this.saveAndFlush(notification);

        return notification.toDto();
    }

    @Transactional(readOnly = false)
    default NotificationDto read(UUID key) throws EntityNotFoundException {
        final NotificationEntity notification = this.findOneByKey(key).orElse(null);

        if (notification == null) {
            throw new EntityNotFoundException();
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(ZonedDateTime.now());
        }

        this.saveAndFlush(notification);

        return notification.toDto();
    }

}
