package ludo.mentis.aciem.tabellarius.validation;

import ludo.mentis.aciem.tabellarius.model.AttachmentDTO;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UniqueFileNameValidatorTest {

    private final UniqueFileNameValidator validator = new UniqueFileNameValidator();

    @Test
    void testValidUniqueFileNames() {
        var attachments = List.of(
                new AttachmentDTO("file1.txt", "text/plain", new byte[] { 1, 2, 3 }),
                new AttachmentDTO("file2.txt", "text/plain", new byte[] { 1, 2, 3 }),
                new AttachmentDTO("file3.txt", "text/plain", new byte[] { 1, 2, 3 })
        );
        var context = mock(ConstraintValidatorContext.class);
        assertTrue(validator.isValid(attachments, context));
    }

    @Test
    void testInvalidDuplicateFileNames() {
        var attachments = List.of(
                new AttachmentDTO("file1.txt", "text/plain", new byte[] { 1, 2, 3 }),
                new AttachmentDTO("file2.txt", "text/plain", new byte[] { 1, 2, 3 }),
                new AttachmentDTO("file1.txt", "text/plain", new byte[] { 1, 2, 3 })
        );
        var context = mock(ConstraintValidatorContext.class);
        assertFalse(validator.isValid(attachments, context));
    }

    @Test
    void testEmptyAttachments() {
        List<AttachmentDTO> attachments = List.of();
        var context = mock(ConstraintValidatorContext.class);
        assertTrue(validator.isValid(attachments, context));
    }

    @Test
    void testNullAttachments() {
        var context = mock(ConstraintValidatorContext.class);
        assertTrue(validator.isValid(null, context));
    }
}