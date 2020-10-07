package eu.opertusmundi.message.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Message command object")
@NoArgsConstructor
public class MessageCommandDto extends BaseMessageCommandDto {

    @Schema(description = "Sender unique id", required = true)
    @NotNull
    @Getter
    @Setter
    private UUID sender;

    @Schema(description = "Reply to the message with the specified id", required = false)
    @Getter
    @Setter
    private UUID message;

}
