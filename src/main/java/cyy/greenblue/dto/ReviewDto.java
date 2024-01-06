package cyy.greenblue.dto;

import cyy.greenblue.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewDto {
    private long id;
    private int score;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private Long orderProductId;
    private List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

    @Builder
    public ReviewDto(long id, int score, String title, String content, LocalDateTime regDate,
                     Long orderProductId, List<ReviewImgDto> reviewImgDtoList) {
        this.id = id;
        this.score = score;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.orderProductId = orderProductId;
        this.reviewImgDtoList = reviewImgDtoList;
    }

    public ReviewDto toDto(Review review, List<ReviewImgDto> reviewImgDtoList) {
        return ReviewDto.builder()
                .id(review.getId())
                .score(review.getScore())
                .title(review.getTitle())
                .content(review.getContent())
                .regDate(review.getRegDate())
                .orderProductId(review.getOrderProduct().getId())
                .reviewImgDtoList(reviewImgDtoList)
                .build();
    }
}
