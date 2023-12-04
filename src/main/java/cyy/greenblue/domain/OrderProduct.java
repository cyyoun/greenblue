package cyy.greenblue.domain;

import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import lombok.Getter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_status")
    private PurchaseStatus purchaseStatus;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_sheet_id")
    private OrderSheet orderSheet;

    public OrderProduct() {
        this.purchaseStatus = PurchaseStatus.PURCHASE_UNCONFIRM;
        this.reviewStatus = ReviewStatus.UNWRITTEN;
    }

    public void updatePurchaseStatus(PurchaseStatus purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }
    public void updatePurchaseConfirm(PurchaseStatus purchaseStatus, LocalDateTime purchaseDate) {
        this.purchaseStatus = purchaseStatus;
        this.purchaseDate = purchaseDate;
    }

    public void updateOrderSheet(OrderSheet orderSheet) {
        this.orderSheet = orderSheet;
    }

    public void updateReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
