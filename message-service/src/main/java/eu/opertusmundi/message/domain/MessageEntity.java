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

import eu.opertusmundi.message.model.MessageDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Message")
@Table(name = "`message`", schema = "messaging")
public class MessageEntity {

    @Id
    @SequenceGenerator(schema = "messaging", sequenceName = "message_id_seq", name = "message_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "message_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "`id`")
    @Getter
    private Integer id;

    @NotNull
    @Column(name = "key", updatable = false, columnDefinition = "uuid")
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private UUID key;

    @NotNull
    @Column(name = "thread", updatable = false, columnDefinition = "uuid")
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private UUID thread;

    @NotNull
    @Column(name = "owner", updatable = false, columnDefinition = "uuid")
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private UUID owner;

    @NotNull
    @Column(name = "sender", updatable = false, columnDefinition = "uuid")
    @Getter
    @Setter
    private UUID sender;

    @NotNull
    @Column(name = "recipient", updatable = false, columnDefinition = "uuid")
    @Getter
    @Setter
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
    ZonedDateTime readAt;

    protected MessageEntity() {

    }

    public MessageEntity(UUID owner, UUID key, UUID thread) {
        this.owner  = owner;
        this.key    = key;
        this.thread = thread;
    }

    public MessageDto toDto() {
        final MessageDto n = new MessageDto();

        n.setCreatedAt(this.sendAt);
        n.setId(this.key);
        n.setRead(this.read);
        n.setReadAt(this.readAt);
        n.setRecipient(this.recipient);
        n.setText(this.text);
        n.setThread(this.thread);

        return n;
    }

}