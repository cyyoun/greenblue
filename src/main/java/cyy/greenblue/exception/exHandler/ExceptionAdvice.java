package cyy.greenblue.exception.exHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import cyy.greenblue.exception.CategoryException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ExceptionInfo validExHandler(ValidationException e) {
        return ExceptionInfo.builder()
                .code("400")
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryException.class)
    public ExceptionInfo categoryExHandler(CategoryException e) {
        return ExceptionInfo.builder()
                .code("400")
                .message(e.getMessage())
                .build();
    }

}
