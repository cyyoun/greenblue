package cyy.greenblue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDto {
    private Long id;
    private int points;
    private long reviewId;
    private long orderProductId;
}
