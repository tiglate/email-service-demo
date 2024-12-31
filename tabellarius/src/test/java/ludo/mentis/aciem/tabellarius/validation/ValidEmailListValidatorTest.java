package ludo.mentis.aciem.tabellarius.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidEmailListValidatorTest {

    private ValidEmailListValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidEmailListValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void testValidEmails() {
        var emails = List.of("test1@example.com", "test2@example.com", "test3@example.com");
        assertTrue(validator.isValid(emails, context));
    }

    @Test
    void testInvalidEmails() {
        var emails = List.of("test1@example.com", "invalid-email", "test3@example.com");
        assertFalse(validator.isValid(emails, context));
    }

    @Test
    void testDuplicateEmails() {
        var emails = List.of("test1@example.com", "test2@example.com", "test1@example.com");
        assertFalse(validator.isValid(emails, context));
    }

    @Test
    void testEmptyEmails() {
        var annotation = mock(ValidEmailList.class);
        when(annotation.allowEmpty()).thenReturn(true);
        validator.initialize(annotation);

        List<String> emails = List.of();
        assertTrue(validator.isValid(emails, context));
    }

    @Test
    void testNullEmails() {
        var annotation = mock(ValidEmailList.class);
        when(annotation.allowNull()).thenReturn(true);
        validator.initialize(annotation);

        assertTrue(validator.isValid(null, context));
    }
}