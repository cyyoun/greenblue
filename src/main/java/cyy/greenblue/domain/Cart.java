package cyy.greenblue.domain;

import lombok.Getter;

import jakarta.persistence.*;

@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    private int quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Cart() {}

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateCart(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

}
