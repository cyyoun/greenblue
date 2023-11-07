package cyy.greenblue.domain;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String code;
    private int price;
    @Column(name = "sold_out")
    private boolean soldOut; //true: 품절, false: 판매중
    private String color;
    private String size;

    @ManyToOne
    @JoinColumn(name = "bottom_category_id")
    private BottomCategory bottomCategory; //단방향 fk
}
