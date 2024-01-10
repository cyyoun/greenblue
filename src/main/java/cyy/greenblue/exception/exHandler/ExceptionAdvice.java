package cyy.greenblue.exception.exHandler;

import cyy.greenblue.exception.*;
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
        e.printStackTrace();
        return ExceptionInfo.builder().code("400").message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryException.class)
    public ExceptionInfo categoryExHandler(CategoryException e) {
        e.printStackTrace();
        return ExceptionInfo.builder().code("400").message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ImgSaveFailException.class)
    public ExceptionInfo imgSaveFailExHandler(ImgSaveFailException e) {
        e.printStackTrace();
        return ExceptionInfo.builder().code("500").message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoMainImgException.class)
    public ExceptionInfo noMainImgExHandler(NoMainImgException e) {
        e.printStackTrace();
        return ExceptionInfo.builder().code("400").message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SoldOutException.class)
    public ExceptionInfo soldOutExHandler(SoldOutException e) {
        e.printStackTrace();
        return ExceptionInfo.builder().code("400").message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PurchaseConfirmException.class)
    public ExceptionInfo orderProductExHandler(PurchaseConfirmException e) {
        e.printStackTrace();
        return ExceptionInfo.builder().code("400").message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReviewException.class)
    public ExceptionInfo reviewExHandler(ReviewException e) {
        e.printStackTrace();
        return ExceptionInfo.builder().code("400").message(e.getMessage()).build();
    }
}
