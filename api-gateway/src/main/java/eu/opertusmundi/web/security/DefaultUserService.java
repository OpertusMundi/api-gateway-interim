package eu.opertusmundi.web.security;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.BasicMessageCode;
import eu.opertusmundi.common.model.EnumActivationStatus;
import eu.opertusmundi.common.model.EnumActivationTokenType;
import eu.opertusmundi.common.model.EnumAuthProvider;
import eu.opertusmundi.common.model.EnumRole;
import eu.opertusmundi.common.model.ServiceResponse;
import eu.opertusmundi.common.model.dto.AccountCreateCommandDto;
import eu.opertusmundi.common.model.dto.AccountDto;
import eu.opertusmundi.common.model.dto.AccountProfileUpdateCommandDto;
import eu.opertusmundi.common.model.dto.ActivationTokenCommandDto;
import eu.opertusmundi.common.model.dto.ActivationTokenDto;
import eu.opertusmundi.common.model.dto.AddressCommandDto;
import eu.opertusmundi.web.domain.AccountEntity;
import eu.opertusmundi.web.domain.ActivationTokenEntity;
import eu.opertusmundi.web.domain.AddressEntity;
import eu.opertusmundi.web.domain.ProfileProviderEmbeddable;
import eu.opertusmundi.web.feign.client.EmailServiceFeignClient;
import eu.opertusmundi.web.model.email.MailActivationModel;
import eu.opertusmundi.web.model.email.MessageDto;
import eu.opertusmundi.web.repository.AccountProfileHistoryRepository;
import eu.opertusmundi.web.repository.AccountRepository;
import eu.opertusmundi.web.repository.ActivationTokenRepository;
import feign.FeignException;

@Service
public class DefaultUserService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUserService.class);

    @Value("${opertus-mundi.base-url}")
    private String baseUrl;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountProfileHistoryRepository accountProfileHistoryRepository;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private ObjectProvider<EmailServiceFeignClient> emailClient;

    @Override
    @Transactional
    public Optional<AccountDto> findOneByUserName(String username) throws UsernameNotFoundException {
        final AccountEntity account = this.accountRepository.findOneByEmail(username).orElse(null);

        return Optional.ofNullable(account == null ? null : account.toDto());
    }

    @Override
    @Transactional
    public Optional<AccountDto> findOneByUserName(String username, EnumAuthProvider provider) throws UsernameNotFoundException {
        final AccountEntity account = this.accountRepository.findOneByEmailAndProvider(username, provider).orElse(null);

        return Optional.ofNullable(account == null ? null : account.toDto());
    }

    @Override
    public AccountDto createAccount(AccountCreateCommandDto command) {
        // Create account
        final AccountDto account = this.accountRepository.create(command);

        // Create activation token for account email
        final ActivationTokenDto accountToken = this.activationTokenRepository.create(
            account.getId(), account.getEmail(), 1, EnumActivationTokenType.ACCOUNT
        );
        // Send activation link to client
        this.sendMail(account.getProfile().getFullName(), accountToken);

        return account;
    }

    @Override
    public ServiceResponse<ActivationTokenDto> createToken(ActivationTokenCommandDto command) {
        final AccountEntity account = this.accountRepository.findOneByEmail(command.getEmail()).orElse(null);

        logger.info("Request token for email {}", command.getEmail());

        if (account == null) {
            return ServiceResponse.success();
        }

        final boolean                 activated = account.getActivationStatus() == EnumActivationStatus.COMPLETED;
        final EnumActivationTokenType type      = activated ? EnumActivationTokenType.PROVIDER : EnumActivationTokenType.ACCOUNT;

        switch (type) {
            case ACCOUNT :
                if (!command.getEmail().equals(account.getEmail())) {
                    // Invalid email
                    return ServiceResponse.error(BasicMessageCode.EmailNotFound, "Email not registered to the account");
                }
                break;
            case PROVIDER :
                if (!command.getEmail().equals(account.getProfile().getProvider().getEmail())) {
                    // Invalid email
                    return ServiceResponse.error(BasicMessageCode.EmailNotFound, "Email not registered to the profile");
                }
                break;
        }

        // Create activation token
        final ActivationTokenDto token = this.activationTokenRepository.create(account.getId(), command.getEmail(), 1, type);
        // Send activation link to client
        this.sendMail(account.getFullName(), token);

        return ServiceResponse.result(token);
    }

    @Override
    @Transactional
    public ServiceResponse<Void> redeemToken(UUID token) {
        final ActivationTokenEntity tokenEntity = this.activationTokenRepository.findOneByToken(token).orElse(null);

        if (tokenEntity == null) {
            return ServiceResponse.error(BasicMessageCode.TokenNotFound, "Token was not found");
        }
        if (tokenEntity.isExpired()) {
            return ServiceResponse.error(BasicMessageCode.TokenIsExpired, "Token has expired");
        }

        final AccountEntity accountEntity = this.accountRepository.findById(tokenEntity.getAccount()).orElse(null);

        if (accountEntity == null) {
            return ServiceResponse.error(BasicMessageCode.AccountNotFound, "Account was not found");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        this.activationTokenRepository.redeem(tokenEntity);

        switch (tokenEntity.getType()) {
            case ACCOUNT :
                if (!tokenEntity.getEmail().equals(accountEntity.getEmail())) {
                    return ServiceResponse.error(BasicMessageCode.EmailNotFound, "Email not registered to the account");
                }
                if (accountEntity.getActivationStatus() != EnumActivationStatus.COMPLETED) {
                    // Activate account only once
                    accountEntity.setActivatedAt(now);
                    accountEntity.setActivationStatus(EnumActivationStatus.COMPLETED);
                }
                // Always verify account
                accountEntity.setEmailVerified(true);
                accountEntity.setEmailVerifiedAt(now);
                break;
            case PROVIDER :
                final ProfileProviderEmbeddable providerEntity = accountEntity.getProfile().getProvider();

                if (!tokenEntity.getEmail().equals(providerEntity.getEmail())) {
                    return ServiceResponse.error(BasicMessageCode.EmailNotFound, "Email not registered to the provider");
                }
                // Verify email only for registered accounts
                if (accountEntity.getActivationStatus() == EnumActivationStatus.COMPLETED) {
                    providerEntity.setEmailVerified(true);
                    providerEntity.setEmailVerifiedAt(now);
                }
                break;
        }

        this.accountRepository.save(accountEntity);

        return ServiceResponse.success();
    }

    @Override
    @Transactional
    public AccountDto updateProfile(AccountProfileUpdateCommandDto command) {
        final AccountEntity accountEntity = this.accountRepository.findById(command.getId()).orElse(null);

        // Create history record
        this.accountProfileHistoryRepository.createSnapshot(accountEntity.getProfile().getId());

        // Update profile data
        final AccountDto account = this.accountRepository.updateProfile(command);

        return account;
    }

    @Override
    @Transactional
    public void grant(AccountDto account, AccountDto grantedby, EnumRole... roles) {
        Assert.notNull(account, "Expected a non-null account");
        Assert.notEmpty(roles, "Expected at least 1 role");

        final Optional<AccountEntity> accountEntity = this.accountRepository.findById(account.getId());
        if (!accountEntity.isPresent()) {
            return;
        }

        final Optional<AccountEntity> grantedbyEntity = grantedby != null ? this.accountRepository.findById(grantedby.getId())
                : Optional.empty();

        for (final EnumRole role : roles) {
            accountEntity.get().grant(role, grantedbyEntity.isPresent() ? grantedbyEntity.get() : null);
        }

        this.accountRepository.saveAndFlush(accountEntity.get());
    }

    @Override
    @Transactional
    public void revoke(AccountDto account, EnumRole... roles) {
        Assert.notNull(account, "Expected a non-null account");
        Assert.notEmpty(roles, "Expected at least 1 role");

        final Optional<AccountEntity> accountEntity = this.accountRepository.findById(account.getId());
        if (!accountEntity.isPresent()) {
            return;
        }

        for (final EnumRole role : roles) {
            accountEntity.get().revoke(role);
        }

        this.accountRepository.saveAndFlush(accountEntity.get());
    }


    @Override
    @Transactional
    public AccountDto createAddress(AddressCommandDto command) {
        Assert.notNull(command, "Expected a non-null command");

        final AccountEntity account = this.accountRepository.findById(command.getId()).orElse(null);
        if (account == null) {
            throw new EntityNotFoundException();
        }

        account.getProfile().addAddress(command);

        this.accountRepository.saveAndFlush(account);

        return account.toDto();
    }

    @Override
    @Transactional
    public AccountDto updateAddress(AddressCommandDto command) {
        Assert.notNull(command, "Expected a non-null command");

        Assert.notNull(command, "Expected a non-null command");

        final AccountEntity account = this.accountRepository.findById(command.getId()).orElse(null);
        if (account == null) {
            throw new EntityNotFoundException();
        }

        final AddressEntity address = account.getProfile().getAddressByKey(command.getKey()).orElse(null);
        if (address == null) {
            throw new EntityNotFoundException();
        }

        address.update(command);

        this.accountRepository.saveAndFlush(account);

        return account.toDto();
    }

    @Override
    @Transactional
    public AccountDto deleteAddress(AddressCommandDto command) {
        Assert.notNull(command, "Expected a non-null command");

        final AccountEntity account = this.accountRepository.findById(command.getId()).orElse(null);
        if (account == null) {
            throw new EntityNotFoundException();
        }

        account.getProfile().removeAddress(command.getKey());

        this.accountRepository.saveAndFlush(account);

        return account.toDto();
    }

    private void sendMail(String name, ActivationTokenDto token) {
        // Compose message
        final MessageDto<MailActivationModel> message = new MessageDto<>();

        final MailActivationModel model = this.createActivationMailModel(name, token);

        // TODO: Create/Render template, create parameters for subject, sender
        // and URLs

        message.setSubject("Activate account");

        message.setSender("hello@OpertusMundi.eu", "OpertusMundi");

        if (StringUtils.isBlank(name)) {
            message.setRecipients(token.getEmail());
        } else {
            message.setRecipients(token.getEmail(), name);
        }

        message.setTemplate("token-request");

        message.setModel(model);

        try {
            final ResponseEntity<BaseResponse> response = this.emailClient.getObject().sendMail(message);

            if (!response.getBody().getSuccess()) {
                // TODO: Add logging ...
                // TODO: Handle error ...
            }
        } catch (final FeignException fex) {
            // final BasicMessageCode code = BasicMessageCode.fromStatusCode(fex.status());

            // TODO: Add logging ...
            // TODO: Handle error ...
        }
    }

    private MailActivationModel createActivationMailModel(String name, ActivationTokenDto token) {
        final MailActivationModel model = new MailActivationModel();

        model.setName(name);
        model.setToken(token.getToken());
        model.setUrl(this.baseUrl);

        return model;
    }

}