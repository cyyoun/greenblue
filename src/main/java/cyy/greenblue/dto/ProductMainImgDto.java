package cyy.greenblue.dto;

import cyy.greenblue.domain.ProductMainImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductMainImgDto extends ImgDto{

    @Builder
    public ProductMainImgDto(Long id, String filename) {
        super(id, filename);
    }

    @Override
    public ProductMainImgDto toDto(Object img) {
        if (img instanceof ProductMainImg productMainImg) {
            return (ProductMainImgDto) ProductMainImgDto.builder()
                    .id(productMainImg.getId())
                    .filename(productMainImg.getFilename())
                    .build();
        }
        return null;

    }
}
