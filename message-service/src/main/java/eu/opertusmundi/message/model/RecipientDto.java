package eu.opertusmundi.message.model;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Recipient object")
@NoArgsConstructor
@Getter
@Setter
public class RecipientDto {

    @Schema(description = "User unique id")
    private UUID id;

    @Schema(description = "User full name")
    private String name;

}
