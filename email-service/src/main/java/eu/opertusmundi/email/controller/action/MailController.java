package eu.opertusmundi.email.controller.action;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.email.model.MessageDto;
import eu.opertusmundi.email.model.openapi.openapi.schema.MailEndPointTypes.TextResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Mail message endpoint
 */
@Tag(
    name        = "Email",
    description = "The email API"
)
@RequestMapping(path = "/v1/email", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_USER"})
public interface MailController {

    /**
     * Send mail
     *
     * @param message Message configuration object
     *
     * @return An instance of {@link BaseResponse}
     */
    @Operation(
        summary     = "Send mail",
        description = "Sends a mail message to the specified recipients",
        tags        = { "Email" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseResponse.class))
    )
    @PostMapping(value = "/send")
    BaseResponse sendMail(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Message",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageDto.class)),
            required = true
        )
        @RequestBody(required = true) MessageDto message
    );

    /**
     * Renders an email template in text format without sending it
     *
     * @param message Message configuration object
     *
     * @return An instance of {@link RestResponse}
     */
    @Operation(
        summary     = "Render email content",
        description = "Renders an email template in text format without sending it",
        tags        = { "Email" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TextResponse.class))
    )
    @PostMapping(value = "/render/text")
    RestResponse<String> renderText(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Message",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageDto.class)),
            required = true
        )
        @RequestBody(required = true) MessageDto message
    );

    /**
     * Renders an email template in HTML format without sending it
     *
     * @param message Message configuration object
     *
     * @return An instance of {@link RestResponse}
     */
    @Operation(
        summary     = "Render email content",
        description = "Renders an email template in HTML format without sending it",
        tags        = { "Email" }
    )
    @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TextResponse.class))
    )
    @PostMapping(value = "/render/html")
    RestResponse<String> renderHtml(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Message",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MessageDto.class)),
            required = true
        )
        @RequestBody(required = true) MessageDto message
    );

}
