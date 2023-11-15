package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;
    private String name;
    private String code;
    private int price;
    
    @Column(name = "sold_out")
    private boolean soldOut; //true: 품절, false: 판매중
    private String color;
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaf_category_id")
    private LeafCategory leafCategory;

    /** 양방향 관계 고려해보기
     * @OneToMany(mappedBy = "product")
     * private List<ProductPicture> pictures = new ArrayList<>(); 
     */
    
}
