package cyy.greenblue.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PointDto {
    private Long id;
    private int points;
    private long reviewId;
    private long orderProductId;

    public PointDto(Long id, int points, long reviewId, long orderProductId) {
        this.id = id;
        this.points = points;
        this.reviewId = reviewId;
        this.orderProductId = orderProductId;
    }
}
