package ludo.mentis.aciem.auctoritas.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(String source) {
        try {
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return OffsetDateTime.of(LocalDateTime.parse(source, formatter), OffsetDateTime.now().getOffset());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format: yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}