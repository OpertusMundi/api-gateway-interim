package eu.opertusmundi.bpm.server.delegate;

import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A basic example of a Spring-managed bean used as a {@link JavaDelegate} in a BPM service task.
 */
@Component()
public class FooTask implements JavaDelegate
{
    private static final Logger logger = LoggerFactory.getLogger(FooTask.class);

    public FooTask()
    {
        logger.info("== Another instance is created");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        final String businessKey = execution.getBusinessKey();
        final String activityInstanceId = execution.getActivityInstanceId();

        logger.info("== Executing activity {} with businessKey {}", activityInstanceId, businessKey);

        System.err.println("FooTask variables:");
        for (final Map.Entry<String, Object> e: execution.getVariables().entrySet()) {
            System.err.printf("   - %s: %s%n", e.getKey(), e.getValue());
        }

        final String name1 = (String) execution.getVariable("name1");
        if (name1 == null || name1.isEmpty()) {
            // Report the error as a variable (to be used in a condition in a following XOR gateway)
            // (https://docs.camunda.org/manual/latest/user-guide/process-engine/error-handling/#catch-exception-and-use-data-based-xor-gateway)
            logger.error("Expected a non empty string for `name1`!");
            execution.setVariable("isNameValid", false);
            return;
        } else {
            execution.setVariable("isNameValid", true);
        }

        execution.setVariable("score", 0.81713);
    }

}
