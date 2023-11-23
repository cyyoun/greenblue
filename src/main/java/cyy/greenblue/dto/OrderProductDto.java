package cyy.greenblue.dto;

import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.Product;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderProductDto {

    private Long id;
    private int quantity;
    private Long memberId;
    private Long productId;
    private Long orderSheetId;

    public OrderProductDto() {
    }

    public OrderProductDto(Long id, int quantity, Long memberId, Long productId, Long orderSheetId) {
        this.id = id;
        this.quantity = quantity;
        this.memberId = memberId;
        this.productId = productId;
        this.orderSheetId = orderSheetId;
    }
}
