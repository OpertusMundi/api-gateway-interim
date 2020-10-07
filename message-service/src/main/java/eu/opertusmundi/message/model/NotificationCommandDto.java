package eu.opertusmundi.message.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

@Schema(description = "Notification command object")
@NoArgsConstructor
public class NotificationCommandDto extends BaseMessageCommandDto {

}
