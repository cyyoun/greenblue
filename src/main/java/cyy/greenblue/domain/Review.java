package cyy.greenblue.domain;

import lombok.Getter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    public Review() {
        this.regDate = LocalDateTime.now();
    }

    public void updateReview(int score, String title, String content) {
        this.score = score;
        this.title = title;
        this.content = content;

    }
}
