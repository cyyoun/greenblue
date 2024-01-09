package cyy.greenblue.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String name;
    private String code;
    private int price;
    private int quantity;
    private String description;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static ProductBuilder productBuilder() {
        return new ProductBuilder()
                .code(UUID.randomUUID().toString())
                .regDate(LocalDateTime.now());
    }

    public Product() {
        this.code = UUID.randomUUID().toString();
        this.regDate = LocalDateTime.now();
    }

    public void update(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.category = product.getCategory();
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
