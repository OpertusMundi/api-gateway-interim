package eu.opertusmundi.message.domain;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.opertusmundi.message.model.NotificationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Notification")
@Table(name = "`notification`", schema = "messaging")
public class NotificationEntity {

    @Id
    @SequenceGenerator(schema = "messaging", sequenceName = "notification_id_seq", name = "notification_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "notification_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "`id`")
    @Getter
    private Integer id;

    @NotNull
    @Column(name = "key", updatable = false, columnDefinition = "uuid")
    @Getter
    private final UUID key = UUID.randomUUID();

    @NotNull
    @Column(name = "recipient", updatable = false, columnDefinition = "uuid")
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private UUID recipient;

    @NotEmpty
    @Column(name = "`text`")
    @Getter
    @Setter
    private String text;

    @NotNull
    @Column(name = "`send_at`")
    @Getter
    final ZonedDateTime sendAt = ZonedDateTime.now();

    @Column(name = "`read`")
    @Getter
    @Setter
    boolean read = false;


    @Column(name = "`read_at`")
    @Getter
    @Setter
    ZonedDateTime readAt = ZonedDateTime.now();

    protected NotificationEntity() {

    }

    public NotificationEntity(UUID recipient) {
        this.recipient = recipient;
    }

    public NotificationDto toDto() {
        final NotificationDto n = new NotificationDto();

        n.setCreatedAt(this.sendAt);
        n.setId(this.key);
        n.setRead(this.read);
        n.setReadAt(this.readAt);
        n.setRecipient(this.recipient);
        n.setText(this.text);

        return n;
    }

}