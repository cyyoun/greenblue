package cyy.greenblue.dto;

import cyy.greenblue.domain.Review;
import cyy.greenblue.domain.ReviewImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewImgDto {
    private Long id;
    private String filename;

    @Builder
    public ReviewImgDto(Long id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    public ReviewImgDto toDto(ReviewImg reviewImg) {
        return ReviewImgDto.builder()
                .id(reviewImg.getId())
                .filename(reviewImg.getFilename())
                .build();
    }
}
