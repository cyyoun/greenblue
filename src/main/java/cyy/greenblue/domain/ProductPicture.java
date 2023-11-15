package cyy.greenblue.domain;

import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class ProductPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_picture_id")
    private long id;
    private String fileName; //파일명
    private String fileDir; //파일경로
    private String fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


}

