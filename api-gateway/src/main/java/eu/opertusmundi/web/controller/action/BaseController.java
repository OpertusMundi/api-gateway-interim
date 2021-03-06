package eu.opertusmundi.web.controller.action;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import eu.opertusmundi.web.security.AuthenticationFacade;

public abstract class BaseController {

    @Autowired
    protected AuthenticationFacade authenticationFacade;

    protected boolean isAuthenticated() {
        return this.authenticationFacade.isAuthenticated();
    }

    protected Integer currentUserId() {
        return this.authenticationFacade.getCurrentUserId();
    }

    protected UUID currentUserKey() {
        return this.authenticationFacade.getCurrentUserKey();
    }

    protected String currentUserEmail() {
        return this.authenticationFacade.getCurrentUserEmail();
    }

    protected void ensureRegistered() throws AccessDeniedException {
        if (!this.authenticationFacade.isRegistered()) {
            throw new AccessDeniedException("Access Denied");
        }
    }

}
