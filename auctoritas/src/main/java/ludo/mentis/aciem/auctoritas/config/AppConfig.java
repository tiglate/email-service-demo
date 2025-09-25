package ludo.mentis.aciem.auctoritas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Configuration
@ComponentScans(value = {
        @ComponentScan("ludo.mentis.aciem.auctoritas"),
        @ComponentScan("ludo.mentis.aciem.commons.web")
})
public class AppConfig {

    @Bean
    UriBuilder uriBuilder() {
        return new DefaultUriBuilderFactory().builder();
    }
}
