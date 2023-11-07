package cyy.greenblue.domain;

import javax.persistence.*;

@Entity
public class ProductPicture {
    @Id
    @Column(name = "product_picture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name; //파일명
    private String path; //파일경로
    private int size;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //단방향 fk


}

