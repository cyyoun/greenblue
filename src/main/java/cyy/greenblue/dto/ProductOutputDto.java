package cyy.greenblue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;


@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOutputDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @Range(min = 1000, max = 9999999)
    private int price;
    @NotNull
    private ProductMainImgDto mainImgDto;
}
