package cyy.greenblue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewOutputDto {
    private long id;
    private String username;
    @Range(min = 1, max = 10)
    private int score;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private LocalDateTime regDate;
    @NotNull
    private Long orderProductId;
    private List<ReviewImgDto> reviewImgDtoList;
}
