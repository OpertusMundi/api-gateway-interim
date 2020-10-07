package eu.opertusmundi.message.controller.action;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.message.model.NotificationCommandDto;
import eu.opertusmundi.message.model.openapi.schema.NotificationEndpointTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Notifications endpoint
 */
@Tag(
    name        = "Notification",
    description = "The notification API"
)
@RequestMapping(path = "/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public interface NotificationController {

    /**
     * Find notifications
     *
     * @param pageIndex
     * @param pageSize
     * @param userKey
     * @param dateFrom
     * @param dateTo
     * @param read
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Find notifications",
        tags        = { "Notification" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NotificationEndpointTypes.NotificationListResponseDto.class)
        )
    )
    @GetMapping(value = "")
    @Secured({"ROLE_USER"})
    RestResponse<?> findNotifications(
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Page index",
            schema      = @Schema(type = "integer", defaultValue = "0")
        )
        @RequestParam(name = "page", required = false) Integer pageIndex,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Page size",
            schema      = @Schema(type = "integer", defaultValue = "10")
        )
        @RequestParam(name = "size", required = false) Integer pageSize,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Filter user by key"
        )
        @RequestParam(name = "user", required = true) UUID userKey,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Filter notifications after date"
        )
        @RequestParam(name = "date-from", required = false) ZonedDateTime dateFrom,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Filter notifications before date"
        )
        @RequestParam(name = "date-to", required = false) ZonedDateTime dateTo,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Filter read notifications"
        )
        @RequestParam(name = "read", required = false) Boolean read
    );

    /**
     * Send notification
     *
     * @param Notification Notification command object
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Send a notification to a user",
        tags        = { "Notification" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NotificationEndpointTypes.NotificationResponseDto.class)
        )
    )
    @PostMapping(value = "")
    @Secured({"ROLE_USER"})
    RestResponse<?> sendNotification(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Notification command object",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotificationCommandDto.class)),
            required = true
        )
        @RequestBody(required = true) NotificationCommandDto notification
    );

    /**
     * Mark notification as read
     *
     * @param key The key of the notification to mark as read
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Read notification",
        description = "Marks a notification as read",
        tags        = { "Notification" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class))
    )
    @PutMapping(value = "/{key}")
    @Secured({"ROLE_USER"})
    BaseResponse readNotification(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Notification unique key"
        )
        @PathVariable(name = "key", required = true) UUID key
    );

}
