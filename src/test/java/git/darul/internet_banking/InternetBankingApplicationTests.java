package git.darul.internet_banking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class InternetBankingApplicationTests {

    @Test
    void contextLoads() {
        assertNotNull(this, "Spring Application Context should be loaded successfully");
    }
}