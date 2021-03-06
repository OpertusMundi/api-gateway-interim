package eu.opertusmundi.web.controller.action;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.EnumActivationTokenType;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.common.model.ServiceResponse;
import eu.opertusmundi.common.model.dto.AccountCommandDto;
import eu.opertusmundi.common.model.dto.AccountDto;
import eu.opertusmundi.common.model.dto.ActivationTokenCommandDto;
import eu.opertusmundi.common.model.dto.ActivationTokenDto;
import eu.opertusmundi.web.model.security.Token;
import eu.opertusmundi.web.security.UserService;
import eu.opertusmundi.web.validation.AccountValidator;

@RestController
public class AccountControllerImpl extends BaseController implements AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountControllerImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountValidator accountValidator;

    @Override
    public RestResponse<Token> loggedIn(HttpSession session, CsrfToken token) {
        return RestResponse.result(new Token(token));
    }

    @Override
    public RestResponse<Token> loggedOut(HttpSession session, CsrfToken token) {
        return RestResponse.result(new Token(token));
    }

    @Override
    public BaseResponse register(AccountCommandDto command, BindingResult validationResult) {
        this.accountValidator.validate(command, validationResult);

        if (validationResult.hasErrors()) {
            return RestResponse.invalid(validationResult.getFieldErrors());
        }

        final AccountDto account = this.userService.createAccount(command).getResult().getAccount();

        return RestResponse.result(account);
    }

    @Override
    public BaseResponse requestActivationToken(ActivationTokenCommandDto command, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            return RestResponse.invalid(validationResult.getFieldErrors());
        }

        final ServiceResponse<ActivationTokenDto> response = this.userService.createToken(EnumActivationTokenType.ACCOUNT, command);

        if (response.getResult() == null) {
            return RestResponse.error(response.getMessages());
        }

        return RestResponse.success();
    }

    @Override
    public BaseResponse verifyActivationToken(UUID token) {
        logger.info("Redeem token {}", token);

        final ServiceResponse<Void> response = this.userService.redeemToken(token);

        if (response.getMessages().isEmpty()) {
            return RestResponse.success();
        }

        return RestResponse.error(response.getMessages());
    }

    @Override
    public RestResponse<AccountDto> getUserData() {
        final String email = this.authenticationFacade.getCurrentUserEmail();

        // Refresh profile for each request since the account object stored in the
        // security context may have stale data
        final AccountDto account = this.userService.findOneByUserName(email).orElse(null);

        return RestResponse.result(account);
    }

}
