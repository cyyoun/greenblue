package cyy.greenblue.util;

import jakarta.validation.ValidationException;
import org.springframework.validation.BindingResult;

public class ValidationUtil {
    public static void chkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().getDefaultMessage());
        }
    }
}
