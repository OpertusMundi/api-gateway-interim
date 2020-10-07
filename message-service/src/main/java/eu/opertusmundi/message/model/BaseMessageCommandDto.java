package eu.opertusmundi.message.model;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class BaseMessageCommandDto {

    @Schema(description = "Recipient unique id", required = true)
    @NotNull
    @Getter
    @Setter
    private UUID recipient;

    @Schema(description = "Message text", required = true)
    @NotEmpty
    @Getter
    @Setter
    private String text;

}
