package eu.opertusmundi.bpm.server.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component()
public class CreateAccountDelegate implements JavaDelegate
{
    private static final Logger logger = LoggerFactory.getLogger(CreateAccountDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("Create account");
    }

}
