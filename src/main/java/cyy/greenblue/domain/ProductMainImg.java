package cyy.greenblue.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductMainImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_main_img_id")
    private Long id;
    private String filename; //이미지 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductMainImg(String filename, Product product) {
        this.filename = filename;
        this.product = product;
    }

    public ProductMainImg update(String filename) {
        this.filename = filename;
        return this;
    }
}
