package cyy.greenblue.exception.exHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionInfo {
    private LocalDateTime timestamp;
    private boolean success;
    private String message;
    private String code;

    public ExceptionInfo() {
        this.timestamp = LocalDateTime.now();
    }

    @Builder
    public ExceptionInfo(String code, String message) {
        this.timestamp = LocalDateTime.now();
        this.success = false;
        this.code = code;
        this.message = message;
    }
}
