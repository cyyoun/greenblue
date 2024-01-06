package cyy.greenblue.domain;

import lombok.Getter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    private List<ReviewImg> reviewImgList = new ArrayList<>();

    public Review() {
        this.regDate = LocalDateTime.now();
    }

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateReview(Review review) {
        this.score = review.getScore();
        this.title = review.getTitle();
        this.content = review.getContent();
    }
}
