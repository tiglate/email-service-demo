package ludo.mentis.aciem.tabellarius.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Configuration
@EnableAsync
@ComponentScans(value = {
        @ComponentScan("ludo.mentis.aciem.tabellarius"),
        @ComponentScan("ludo.mentis.aciem.commons.web"),
        @ComponentScan("ludo.mentis.aciem.commons.security")
})
@EnableFeignClients(basePackages = "ludo.mentis.aciem.commons.security.client")
public class AppConfig {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    UriBuilder uriBuilder() {
        return new DefaultUriBuilderFactory().builder();
    }
}