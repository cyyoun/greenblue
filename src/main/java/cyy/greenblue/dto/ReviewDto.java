package cyy.greenblue.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private long id;
    private int score;
    private String title;
    private String content;
}
