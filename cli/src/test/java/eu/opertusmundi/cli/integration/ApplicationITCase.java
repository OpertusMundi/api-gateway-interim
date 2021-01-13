package eu.opertusmundi.cli.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("testing")
public class ApplicationITCase {

    @Test
    @DisplayName(value = "Context Loads")
    public void contextLoads() throws Exception {

    }

}
