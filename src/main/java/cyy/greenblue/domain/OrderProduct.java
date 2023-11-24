package cyy.greenblue.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_status")
    private PointStatus pointStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_sheet_id")
    private OrderSheet orderSheet;

    public void updateOrderSheet(OrderSheet orderSheet) {
        this.orderSheet = orderSheet;
    }

    public void updatePointStatus(PointStatus pointStatus) {
        this.pointStatus = pointStatus;
    }
}
