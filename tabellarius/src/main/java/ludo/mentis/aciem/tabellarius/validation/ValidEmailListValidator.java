package ludo.mentis.aciem.tabellarius.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class ValidEmailListValidator implements ConstraintValidator<ValidEmailList, List<String>> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private boolean allowNull;
    private boolean allowEmpty;

    @Override
    public void initialize(ValidEmailList constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    @Override
    public boolean isValid(List<String> emails, ConstraintValidatorContext context) {
        if (emails == null) {
            return allowNull;
        }
        if (emails.isEmpty()) {
            return allowEmpty;
        }
        var uniqueEmails = new HashSet<>();
        for (var email : emails) {
            if (!EMAIL_PATTERN.matcher(email).matches() || !uniqueEmails.add(email)) {
                return false;
            }
        }
        return true;
    }
}