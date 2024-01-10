package cyy.greenblue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;


@Getter
public class ReviewInputDto {
    @Range(min = 1, max = 5)
    private int score;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long orderProductId;
}
