package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long id;
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewImg() {
    }

    public ReviewImg(String filename, Review review) {
        this.filename = filename;
        this.review = review;
    }

    public ReviewImg update(String filename) {
        this.filename = filename;
        return this;
    }

}
