package cyy.greenblue.domain;

import lombok.Getter;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long id;
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewImg(String filename, Review review) {
        this.filename = filename;
        this.review = review;
    }

    public ReviewImg update(String filename) {
        this.filename = filename;
        return this;
    }

}
