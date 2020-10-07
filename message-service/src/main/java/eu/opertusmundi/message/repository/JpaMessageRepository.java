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

import eu.opertusmundi.message.domain.MessageEntity;
import eu.opertusmundi.message.model.MessageCommandDto;
import eu.opertusmundi.message.model.MessageDto;

@Repository
@Transactional(readOnly = true)
public interface JpaMessageRepository extends JpaRepository<MessageEntity, Integer> {

    Optional<MessageEntity> findOneByOwnerAndKey(UUID owner, UUID key);

    @Query("SELECT m FROM Message m WHERE m.owner = :userKey")
    List<MessageEntity> findUserMessages(@Param("userKey") UUID userKey, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.owner = :userKey and m.read = false")
    List<MessageEntity> findUserUnreadMessages(@Param("userKey") UUID userKey, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.owner = :owner and m.thread = :thread")
    List<MessageEntity> findThreadMessages(@Param("owner") UUID owner, @Param("thread") UUID thread);

    @Query("SELECT m FROM Message m WHERE " +
           "   (m.owner = :userKey) AND " +
           "   (CAST(:dateFrom AS date) IS NULL OR m.sendAt >= :dateFrom) AND " +
           "   (CAST(:dateTo AS date) IS NULL OR m.sendAt <= :dateTo) AND " +
           "   (:read IS NULL OR m.read = :read)")
     Page<MessageEntity> findAll(
         @Param("userKey") UUID userKey,
         @Param("dateFrom") ZonedDateTime dateFrom,
         @Param("dateTo") ZonedDateTime dateTo,
         @Param("read") Boolean read,
         Pageable pageable
     );

    @Transactional(readOnly = false)
    default MessageDto send(MessageCommandDto command) {
        // Unique message key
        final UUID key = UUID.randomUUID();

        // Resolve thread
        final MessageEntity replyMessage = this.findOneByOwnerAndKey(command.getSender(), command.getMessage()).orElse(null);
        final UUID          thread       = replyMessage == null ? key : replyMessage.getThread();

        // Create message for sender
        final MessageEntity senderMessage = new MessageEntity(command.getSender(), key, thread);

        senderMessage.setRecipient(command.getRecipient());
        senderMessage.setSender(command.getSender());
        senderMessage.setText(command.getText());

        this.saveAndFlush(senderMessage);

        // Create message for recipient
        final MessageEntity recipientMessage = new MessageEntity(command.getRecipient(), key, thread);

        recipientMessage.setRecipient(command.getRecipient());
        recipientMessage.setSender(command.getSender());
        recipientMessage.setText(command.getText());

        this.saveAndFlush(recipientMessage);

        return senderMessage.toDto();
    }

    @Transactional(readOnly = false)
    default MessageDto read(UUID owner, UUID key) throws EntityNotFoundException {
        final MessageEntity message = this.findOneByOwnerAndKey(owner, key).orElse(null);

        if (message == null) {
            throw new EntityNotFoundException();
        }

        if (!message.isRead()) {
            message.setRead(true);
            message.setReadAt(ZonedDateTime.now());

            this.saveAndFlush(message);
        }

        return message.toDto();
    }

}
