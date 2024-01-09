package cyy.greenblue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductOutputDto {
    private Long id;
    private String name;
    private int price;
    private String description;
    private LocalDateTime regDate;
    private CategoryDto categoryDto;
    private ProductMainImgDto mainImgDto;
    private List<ProductImgDto> productImgDtoList = new ArrayList<>();
}
