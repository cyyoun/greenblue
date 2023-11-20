package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_picture_id")
    private long id;
    private String fileName; //이미지 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImg() {
    }
    public ProductImg(String fileName, Product product) {
        this.fileName = fileName;
        this.product = product;
    }
}

