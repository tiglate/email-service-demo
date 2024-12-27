package ludo.mentis.aciem.tabellarius.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("ludo.mentis.aciem.tabellarius.domain")
@EnableJpaRepositories("ludo.mentis.aciem.tabellarius.repos")
@EnableTransactionManagement
public class DomainConfig {
}