package ludo.mentis.aciem.auctoritas.config;

import ludo.mentis.aciem.auctoritas.util.StringToOffsetDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToOffsetDateTimeConverter stringToOffsetDateTimeConverter;

    public WebConfig(StringToOffsetDateTimeConverter stringToOffsetDateTimeConverter) {
        this.stringToOffsetDateTimeConverter = stringToOffsetDateTimeConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToOffsetDateTimeConverter);
    }
}