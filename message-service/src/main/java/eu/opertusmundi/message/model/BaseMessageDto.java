package eu.opertusmundi.message.model;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class BaseMessageDto {

    public BaseMessageDto(EnumMessageType type) {
        this.type = type;
    }

    @JsonIgnore
    @Getter
    private final EnumMessageType type;

    @Schema(description = "Message text")
    @NotEmpty
    @Getter
    @Setter
    private String text;

    @Schema(description = "Created at")
    @Getter
    @Setter
    private ZonedDateTime createdAt;

    @Schema(description = "Read at")
    @Getter
    @Setter
    private ZonedDateTime readAt;

    @Schema(description = "Message is marked as read")
    @Getter
    @Setter
    private boolean read;

}
