package ludo.mentis.aciem.auctoritas.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StringToOffsetDateTimeConverterTest {

    private Converter<String, OffsetDateTime> converter;

    @BeforeEach
    void setUp() {
        converter = new StringToOffsetDateTimeConverter();
    }

    @Test
    void testConvertValidDate() {
        String validDate = "2023-10-01T12:30:45";
        OffsetDateTime result = converter.convert(validDate);
        assertNotNull(result);
        assertEquals(2023, result.getYear());
        assertEquals(10, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
        assertEquals(12, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(45, result.getSecond());
    }

    @Test
    void testConvertBlankString() {
        String blankString = " ";
        OffsetDateTime result = converter.convert(blankString);
        assertNull(result);
    }

    @Test
    void testConvertNullString() {
        String nullString = null;
        OffsetDateTime result = converter.convert(nullString);
        assertNull(result);
    }

    @Test
    void testConvertInvalidDate() {
        String invalidDate = "invalid-date";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> converter.convert(invalidDate));
        assertEquals("Invalid date format. Please use the format: yyyy-MM-dd'T'HH:mm:ss", exception.getMessage());
    }

    @Test
    void testConvertEmptyString() {
        String emptyString = "";
        OffsetDateTime result = converter.convert(emptyString);
        assertNull(result);
    }
}