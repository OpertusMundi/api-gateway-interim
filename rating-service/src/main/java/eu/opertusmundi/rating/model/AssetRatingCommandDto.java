package eu.opertusmundi.rating.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Asset rating creation command")
@NoArgsConstructor
public class AssetRatingCommandDto extends BaseRatingCommandDto {

    @Schema(description = "Asset unique id")
    @NotNull
    @Getter
    @Setter
    private UUID asset;

}
