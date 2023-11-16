package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private long id;
    private int quantity;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Cart() {}

    public void editQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void editCart(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

}
