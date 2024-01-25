package cyy.greenblue.domain;

import cyy.greenblue.dto.ReviewInputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private int score;
    private String title;
    private String content;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImg> reviewImgList;

    public void updateReview(ReviewInputDto reviewInputDto) {
        this.score = reviewInputDto.getScore();
        this.title = reviewInputDto.getTitle();
        this.content = reviewInputDto.getContent();
    }
}
