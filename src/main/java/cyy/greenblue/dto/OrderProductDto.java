package cyy.greenblue.dto;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class OrderProductDto {

    private Long id;
    private int quantity;
    private Long productId;
    private Long orderSheetId;
    private String productName;
    private int productPrice;
    private LocalDateTime regDate;
    private ReviewStatus reviewStatus;
    private PurchaseStatus purchaseStatus;

    @Builder
    public OrderProductDto(Long id, int quantity, Long productId, Long orderSheetId,
                           String productName, int productPrice, LocalDateTime regDate,
                           ReviewStatus reviewStatus, PurchaseStatus purchaseStatus) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.orderSheetId = orderSheetId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.regDate = regDate;
        this.reviewStatus = reviewStatus;
        this.purchaseStatus = purchaseStatus;
    }

    public OrderProductDto toDto(OrderProduct orderProduct) {
        return OrderProductDto.builder()
                .id(orderProduct.getId())
                .productName(orderProduct.getProduct().getName())
                .productPrice(orderProduct.getProduct().getPrice())
                .regDate(orderProduct.getOrderSheet().getRegDate())
                .reviewStatus(orderProduct.getReviewStatus())
                .purchaseStatus(orderProduct.getPurchaseStatus())
                .quantity(orderProduct.getQuantity())
                .productId(orderProduct.getProduct().getId())
                .orderSheetId(orderProduct.getOrderSheet().getId())
                .build();
    }
}


