package cyy.greenblue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInputSaveDto {
    @NotBlank
    private String name;
    @Range(min = 1000, max = 9999999)
    private int price;
    @Range(min = 10, max = 9999)
    private int quantity;
    @NotBlank
    private String description;
    @NotNull
    private Integer categoryId;
}
