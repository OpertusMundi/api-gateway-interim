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
import eu.opertusmundi.message.model.MessageCommandDto;
import eu.opertusmundi.message.model.openapi.schema.MessageEndpointTypes;
import eu.opertusmundi.message.model.openapi.schema.MessageEndpointTypes.MessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Messaging endpoint
 */
@Tag(
    name        = "Message",
    description = "The message API"
)
@RequestMapping(path = "/v1/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public interface MessageController {

    /**
     * Find messages
     *
     * @param pageIndex
     * @param pageSize
     * @param userKey
     * @param dateFrom
     * @param dateTo
     * @param read
     *
     * @return An instance of {@link MessageEndpointTypes.MessageListResponseDto}
     */
    @Operation(
        summary     = "Find messages",
        tags        = { "Message" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MessageEndpointTypes.MessageListResponseDto.class)
        )
    )
    @GetMapping(value = "")
    @Secured({"ROLE_USER"})
    RestResponse<?> findMessages(
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
            description = "Filter messages after date"
        )
        @RequestParam(name = "date-from", required = false) ZonedDateTime dateFrom,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Filter messages before date"
        )
        @RequestParam(name = "date-to", required = false) ZonedDateTime dateTo,
        @Parameter(
            in          = ParameterIn.QUERY,
            required    = false,
            description = "Filter read messages"
        )
        @RequestParam(name = "read", required = false) Boolean read
    );

    /**
     * Send message
     *
     * @param message Message configuration object
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Send a message",
        tags        = { "Message" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageResponseDto.class))
    )
    @PostMapping(value = "")
    @Secured({"ROLE_USER"})
    RestResponse<?> sendMessage(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Message command object",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageCommandDto.class)),
            required = true
        )
        @RequestBody(required = true) MessageCommandDto message
    );

    /**
     * Mark message as read
     *
     * @param key The message to mark as read
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Read message",
        description = "Marks a message as read",
        tags        = { "Message" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class))
    )
    @PutMapping(value = "/user/{owner}/message/{key}")
    @Secured({"ROLE_USER"})
    BaseResponse readMessage(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Message owner's unique key"
        )
        @PathVariable(name = "owner", required = true) UUID owner,
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "Message unique key"
        )
        @PathVariable(name = "key", required = true) UUID key
    );

}
