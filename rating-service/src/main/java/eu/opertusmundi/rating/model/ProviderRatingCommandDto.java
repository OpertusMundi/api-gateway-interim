package eu.opertusmundi.rating.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Provider rating creation command")
@NoArgsConstructor
public class ProviderRatingCommandDto extends BaseRatingCommandDto {

    @Schema(description = "Provider unique id")
    @NotNull
    @Getter
    @Setter
    private UUID provider;

}
