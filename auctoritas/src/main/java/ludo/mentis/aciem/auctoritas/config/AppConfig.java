package ludo.mentis.aciem.auctoritas.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans(value = {
        @ComponentScan("ludo.mentis.aciem.auctoritas"),
        @ComponentScan("ludo.mentis.aciem.commons.web")
})
public class AppConfig {

}
