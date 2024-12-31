package ludo.mentis.aciem.tabellarius.validation;

import ludo.mentis.aciem.tabellarius.model.AttachmentDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

public class UniqueFileNameValidator implements ConstraintValidator<UniqueFileName, List<AttachmentDTO>> {

    @Override
    public boolean isValid(List<AttachmentDTO> attachments, ConstraintValidatorContext context) {
        if (attachments == null || attachments.isEmpty()) {
            return true;
        }
        var fileNames = new HashSet<>();
        for (var attachment : attachments) {
            if (!fileNames.add(attachment.getFileName())) {
                return false;
            }
        }
        return true;
    }
}