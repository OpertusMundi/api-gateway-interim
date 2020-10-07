package eu.opertusmundi.rating.controller.action;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.rating.model.AssetRatingCommandDto;
import eu.opertusmundi.rating.model.AssetRatingDto;
import eu.opertusmundi.rating.model.ProviderRatingCommandDto;
import eu.opertusmundi.rating.model.ProviderRatingDto;
import eu.opertusmundi.rating.model.openapi.schema.RatingEndPointTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Rating endpoint
 */
@Tag(
    name        = "Rating",
    description = "The rating API"
)
@RequestMapping(path = "/v1/rating", produces = "application/json")
public interface RatingController {

    /**
     * Get ratings for a single asset
     *
     * @param id Asset unique id
     *
     * @return An instance of {@link RatingEndPointTypes.AssetResponse}
     */
    @Operation(
        summary     = "Get asset ratings",
        description = "Get all ratings for a specific asset",
        tags        = { "Rating" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingEndPointTypes.AssetResponse.class))
    )
    @GetMapping(value = "/asset/{id}")
    RestResponse<List<AssetRatingDto>> getAssetRatings(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Asset unique id"
        )
        @PathVariable(name = "id", required = true) UUID id
    );


    /**
     * Get ratings for a single provider
     *
     * @param id Provider unique id
     *
     * @return An instance of {@link RatingEndPointTypes.ProviderResponse}
     */
    @Operation(
        summary     = "Get provider ratings",
        description = "Get all ratings for a specific provider",
        tags        = { "Rating" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingEndPointTypes.ProviderResponse.class))
    )
    @GetMapping(value = "/provider/{id}")
    RestResponse<List<ProviderRatingDto>> getProviderRatings(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Provider unique id"
        )
        @PathVariable(name = "id", required = true) UUID id
    );

    /**
     * Add asset rating
     *
     * @param id Asset unique id
     * @param command The command object for adding a new rating for an asset
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Add asset rating",
        description = "Adds a new rating for a specific asset",
        tags        = { "Rating" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
    )
    @PostMapping(value = "/asset/{id}")
    BaseResponse addAssetRating(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Asset unique id"
        )
        @PathVariable(name = "id", required = true)
        UUID id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Rating command",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssetRatingCommandDto.class)),
            required = true
        )
        @RequestBody(required = true) AssetRatingCommandDto command
    );

    /**
     * Add provider rating
     *
     * @param id Provider unique id
     * @param command The command object for adding a new rating for a provider
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Add provider rating",
        description = "Adds a new rating for a specific provider",
        tags        = { "Rating" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
    )
    @PostMapping(value = "/provider/{id}")
    BaseResponse addProviderRating(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Provider unique id"
        )
        @PathVariable(name = "id", required = true)
        UUID id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Rating command",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderRatingCommandDto.class)),
            required = true
        )
        @RequestBody(required = true) ProviderRatingCommandDto command
    );

}
