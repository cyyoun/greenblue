package cyy.greenblue.domain;

import cyy.greenblue.domain.status.OrderStatus;
import cyy.greenblue.domain.status.PurchaseStatus;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class OrderSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_sheet_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_status")
    private PurchaseStatus purchaseStatus;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    public OrderSheet() {
        this.orderStatus = OrderStatus.SUCCESS;
        this.regDate = LocalDateTime.now();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updatePurchaseStatus(PurchaseStatus purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }
}
