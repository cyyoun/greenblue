package cyy.greenblue.dto;

import cyy.greenblue.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class BasicProductDto {
    private Long id;
    private String name;
    private int price;
    private ProductMainImgDto mainImgDto;

    @Builder
    public BasicProductDto(long id, String name, int price, ProductMainImgDto mainImgDto) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.mainImgDto = mainImgDto;
    }

    public BasicProductDto toDto(Product product, ProductMainImgDto productMainImgDto) {
        return BasicProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .mainImgDto(productMainImgDto)
                .build();
    }
}
