package eu.opertusmundi.message.model;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class NotificationDto extends BaseMessageDto {

    public NotificationDto() {
        super(EnumMessageType.NOTIFICATION);
    }

    @Schema(description = "Notification unique id")
    @Getter
    @Setter
    private UUID id;

    @Schema(description = "Recipient unique id", required = true)
    @Getter
    @Setter
    private UUID recipient;

}
