package ludo.mentis.aciem.auctoritas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ludo.mentis.aciem.auctoritas.util.StringToOffsetDateTimeConverter;

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