package cyy.greenblue.exception.exHandler;

import cyy.greenblue.exception.CategoryException;
import cyy.greenblue.exception.ImgSaveFailException;
import cyy.greenblue.exception.NoMainImgException;
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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ImgSaveFailException.class)
    public ExceptionInfo imgSaveFailExHandler(ImgSaveFailException e) {
        return ExceptionInfo.builder()
                .code("500")
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoMainImgException.class)
    public ExceptionInfo noMainImgExHandler(NoMainImgException e) {
        e.getStackTrace();
        return ExceptionInfo.builder()
                .code("400")
                .message(e.getMessage())
                .build();
    }
}
