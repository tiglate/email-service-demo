package ludo.mentis.aciem.demo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("ludo.mentis.aciem.demo.domain")
@EnableJpaRepositories("ludo.mentis.aciem.demo.repos")
@EnableTransactionManagement
public class DomainConfig {
}