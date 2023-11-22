package cyy.greenblue.dto;

import cyy.greenblue.domain.OrderProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderRequestDto {
    private String paymentResult;
    private List<OrderProduct> orderProducts;
}
