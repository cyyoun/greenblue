package cyy.greenblue.domain;

import lombok.Getter;

import jakarta.persistence.*;

@Entity
@Getter
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Long id;
    private String filename; //이미지 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImg() {
    }
    public ProductImg(String filename, Product product) {
        this.filename = filename;
        this.product = product;
    }

    public ProductImg update(String filename) {
        this.filename = filename;
        return this;
    }
}

