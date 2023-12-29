package cyy.greenblue.dto;

import cyy.greenblue.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Getter
public class ProductDto {
    private Long id;
    private String name;
    private int price;
    private String description;
    private LocalDateTime regDate;
    private CategoryDto categoryDto;
    private ProductMainImgDto mainImgDto;
    private List<ProductImgDto> productImgDtoList = new ArrayList<>();

    @Builder
    public ProductDto(long id, String name, int price, String description, LocalDateTime regDate,
                      CategoryDto categoryDto, ProductMainImgDto mainImgDto, List<ProductImgDto> productImgDtoList) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.regDate = regDate;
        this.categoryDto = categoryDto;
        this.mainImgDto = mainImgDto;
        this.productImgDtoList = productImgDtoList;
    }

    public ProductDto toDto(Product product, ProductMainImgDto mainImgDto, List<ProductImgDto> productImgDtoList) {
        return toDtoBuilder(product, mainImgDto)
                .productImgDtoList(productImgDtoList)
                .build();
    }

    public ProductDto toDto(Product product, ProductMainImgDto mainImgDto) {
        return toDtoBuilder(product, mainImgDto)
                .build();
    }

    private ProductDtoBuilder toDtoBuilder(Product product, ProductMainImgDto mainImgDto) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .regDate(product.getRegDate())
                .categoryDto(new CategoryDto().toDto(product.getCategory()))
                .mainImgDto(mainImgDto);
    }
}
