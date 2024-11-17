package git.darul.internet_banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InternetBankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(InternetBankingApplication.class, args);
    }
}
