package cyy.greenblue.dto;

import cyy.greenblue.domain.Member;
import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.domain.Product;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderProductDto {

    private long id;
    private int quantity;
    private Member member;
    private Product product;
    private OrderSheet orderSheet;

}
