package cyy.greenblue.dto;

import cyy.greenblue.domain.status.PurchaseStatus;
import cyy.greenblue.domain.status.ReviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class OrderProductDto {

    private Long id;
    private int quantity;
    private Long productId;
    private String mainImgFilename;
    private Long orderSheetId;
    private String productName;
    private int productPrice;
    private LocalDateTime regDate;
    private ReviewStatus reviewStatus;
    private PurchaseStatus purchaseStatus;
}


