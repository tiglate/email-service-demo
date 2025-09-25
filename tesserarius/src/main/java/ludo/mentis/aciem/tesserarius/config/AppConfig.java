package ludo.mentis.aciem.tesserarius.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Configuration
@EnableFeignClients(basePackages = {
        "ludo.mentis.aciem.tesserarius.client",
        "ludo.mentis.aciem.commons.security.client"
})
@ComponentScans(value = {
        @ComponentScan("ludo.mentis.aciem.tesserarius"),
        @ComponentScan("ludo.mentis.aciem.commons.security")
})
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
