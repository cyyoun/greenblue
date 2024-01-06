package cyy.greenblue.dto;

import cyy.greenblue.domain.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CartDto {
    private Long id;
    private int quantity;
    private BasicProductDto basicProductDto;

    @Builder
    public CartDto(Long id, int quantity, BasicProductDto basicProductDto) {
        this.id = id;
        this.quantity = quantity;
        this.basicProductDto = basicProductDto;
    }

    public CartDto toDto(Cart cart, BasicProductDto basicProductDto) {
        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .basicProductDto(basicProductDto)
                .build();
    }
}
