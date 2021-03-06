package eu.opertusmundi.web.controller.action;

import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.common.model.dto.AccountCommandDto;
import eu.opertusmundi.common.model.dto.AccountDto;
import eu.opertusmundi.common.model.dto.ActivationTokenCommandDto;
import eu.opertusmundi.web.model.security.AccountRegisterResponse;
import eu.opertusmundi.web.model.security.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Endpoint for user authentication actions
 */
@Tag(
    name        = "Account",
    description = "The account API"
)
@RequestMapping(produces = "application/json")
public interface AccountController {

    /**
     * Redirect endpoint after a successful forms login operation
     *
     * @param session The injected Spring HTTP session
     * @param token The injected Spring security CSRF token
     * @return An instance of {@link RestResponse} with the new CSRF token
     */
    @Operation(
        operationId = "account-01",
        summary     = "Successful login redirect endpoint",
        description = "Default redirect endpoint for successful forms login operations, used for refreshing the CSRF token",
        tags        = { "Account" },
        security    = {
            @SecurityRequirement(name = "cookie")
        }
    )
    @GetMapping(value = "/logged-in")
    @Secured({"ROLE_USER"})
    RestResponse<Token> loggedIn(
        HttpSession session,
        @Parameter(
            hidden   = true
        )
        CsrfToken token
    );

    /**
     * Redirect endpoint after a successful logout
     *
     * @param session The injected Spring HTTP session
     * @param token The injected Spring security CSRF token
     * @return An instance of {@link RestResponse} with the new CSRF token
     */
    @Operation(
        operationId = "account-02",
        summary     = "Successful logout redirect endpoint",
        description = "Default redirect endpoint for successful logout operations, used for refreshing the CSRF token",
        tags        = { "Account" }
    )
    @GetMapping(value = "/logged-out")
    public RestResponse<Token> loggedOut(
        HttpSession session,
        @Parameter(
            hidden = true
        )
        CsrfToken token
    );

    /**
     * Register new account
     * @param command Account registration command
     *
     * @param validationResult
     * @return
     */
    @Operation(
        operationId = "account-04",
        summary     = "Register account",
        description = "Register a new account",
        tags        = { "Account" }
    )
    @ApiResponse(
        responseCode = "200",
        description  = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountRegisterResponse.class))
    )
    @PostMapping(value = "/action/account/register")
    @Validated
    BaseResponse register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account registration command",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccountCommandDto.class)
            ),
            required = true
        )
        @Valid
        @RequestBody
        AccountCommandDto command,
        @Parameter(
            hidden = true
        )
        BindingResult validationResult
    );

    /**
     * Request account or profile email activation token
     *
     * @param command Activation token command
     * @param validationResult
     * @return
     */
    @Operation(
        operationId = "account-05",
        summary     = "Request activation token",
        description = "Request an activation token for verifying an email or activating an account",
        tags        = { "Account" }
    )
    @ApiResponse(
        responseCode = "200",
        description  = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountRegisterResponse.class))
    )
    @PostMapping(value = "/action/account/token/request")
    @Validated
    BaseResponse requestActivationToken(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Activation token request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivationTokenCommandDto.class)),
            required = true
        )
        @Valid
        @RequestBody
        ActivationTokenCommandDto command,
        @Parameter(
            hidden = true
        )
        BindingResult validationResult
    );

    /**
     * Redeem activation token
     *
     * @param id The activation token to redeem
     * @return
     */
    @Operation(
        operationId = "account-06",
        summary     = "Verify activation token",
        description = "Redeem an activation token to verify an email or activate an account",
        tags        = { "Account" }
    )
    @ApiResponse(
        responseCode = "200",
        description  = "successful operation",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
    )
    @PostMapping(value = "/action/account/token/verify/{token}")
    BaseResponse verifyActivationToken(
        @Parameter(
            in          = ParameterIn.PATH,
            required    = true,
            description = "The token"
        )
        @PathVariable(
            name        = "token",
            required    = true
        )
        UUID token
    );

    /**
     * Get user data
     *
     * @return User data
     */
    @Operation(
        operationId = "account-03",
        summary     = "Get user data",
        description = "Get user data. Roles required: <b>ROLE_USER</b>",
        tags        = { "Account" },
        security    = {
            @SecurityRequirement(name = "cookie")
        }
    )
    @GetMapping(value = "/action/account/user-data")
    @Secured({"ROLE_USER"})
    RestResponse<AccountDto> getUserData();


}
