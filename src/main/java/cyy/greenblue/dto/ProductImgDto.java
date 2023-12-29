package cyy.greenblue.dto;

import cyy.greenblue.domain.ProductImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductImgDto extends ImgDto{

    @Builder
    public ProductImgDto(Long id, String filename) {
        super(id, filename);
    }

    @Override
    public ProductImgDto toDto(Object img) {
        if (img instanceof ProductImg productImg) {
            return (ProductImgDto) ProductImgDto.builder()
                    .id(productImg.getId())
                    .filename(productImg.getFilename())
                    .build();
        }
        return null;

    }
}
