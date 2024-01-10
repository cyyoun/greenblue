package cyy.greenblue.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long id;
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

}
