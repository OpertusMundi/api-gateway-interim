package eu.opertusmundi.rating.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class BaseRatingDto {

    @Schema(description = "Rating value", minimum = "0", maximum = "5")
    @Getter
    @Setter
    protected BigDecimal value;

    @Schema(description = "User comment")
    @Getter
    @Setter
    protected String comment;

    @Schema(description = "Rating date")
    @Getter
    @Setter
    protected ZonedDateTime createdAt;

}
