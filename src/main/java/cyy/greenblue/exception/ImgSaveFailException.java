package cyy.greenblue.exception;

public class ImgSaveFailException extends RuntimeException {
    public ImgSaveFailException(Exception message) {
        super(message);
        message.getStackTrace();
    }
}
