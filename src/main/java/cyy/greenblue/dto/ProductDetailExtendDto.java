package cyy.greenblue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class ProductDetailExtendDto extends ProductExtendDto {
    private List<ProductImgDto> productImgDtoList;

}
