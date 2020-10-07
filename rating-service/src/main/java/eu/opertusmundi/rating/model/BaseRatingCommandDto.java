package eu.opertusmundi.rating.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class BaseRatingCommandDto {

    @Schema(description = "Account unique id")
    @NotNull
    @Getter
    @Setter
    private UUID account;

    @Schema(description = "Rating value", minimum = "0", maximum = "5")
    @NotNull
    @Getter
    @Setter
    protected BigDecimal value;

    @Schema(description = "User comment")
    @Getter
    @Setter
    protected String comment;

}
