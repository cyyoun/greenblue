package cyy.greenblue.dto;

import cyy.greenblue.domain.OrderProduct;
import cyy.greenblue.domain.OrderSheet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderRequestDto {
    private OrderSheet orderSheet;
    private List<OrderProduct> orderProducts;
}
