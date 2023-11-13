package cyy.greenblue.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class CartItem {

    @Column(name = "cart_item_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
